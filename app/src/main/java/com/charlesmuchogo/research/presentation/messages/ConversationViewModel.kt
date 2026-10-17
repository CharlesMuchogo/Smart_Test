package com.charlesmuchogo.research.presentation.messages

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.charlesmuchogo.research.data.local.AppDatabase
import com.charlesmuchogo.research.data.remote.ChatRepository
import com.charlesmuchogo.research.domain.models.SnackBarItem
import com.charlesmuchogo.research.domain.models.TestResult
import com.charlesmuchogo.research.domain.models.chat.ChatMessage
import com.charlesmuchogo.research.domain.models.chat.ChatParticipant
import com.charlesmuchogo.research.domain.models.chat.Conversation
import com.charlesmuchogo.research.domain.models.chat.ConversationType
import com.charlesmuchogo.research.domain.models.chat.MessageKind
import com.charlesmuchogo.research.domain.models.chat.SharedResult
import com.charlesmuchogo.research.domain.viewmodels.SnackBarViewModel
import com.charlesmuchogo.research.navigation.ConversationPage
import com.charlesmuchogo.research.presentation.utils.ResultStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

private val phoneNumberPattern = Regex("""(\+?\d[\s-]?){7,}""")

data class ConversationState(
    val conversationId: Long,
    val conversation: Conversation? = null,
    val messages: List<ChatMessage> = emptyList(),
    val myUserId: Long? = null,
    val isLoading: Boolean = true,
    val isLoadingOlder: Boolean = false,
    val hasMore: Boolean = false,
    val draft: String = "",
    val error: String? = null,
    val results: List<TestResult> = emptyList(),
    val showShareSheet: Boolean = false,
    val reportTarget: ChatMessage? = null,
    val blockTarget: ChatParticipant? = null,
) {
    val type: String
        get() = conversation?.type.orEmpty()

    val otherParticipant: ChatParticipant?
        get() = conversation?.participants?.firstOrNull()

    val showPiiWarning: Boolean
        get() = type != ConversationType.DOCTOR && phoneNumberPattern.containsMatchIn(draft)

    val canShareResults: Boolean
        get() = type == ConversationType.DOCTOR
}

sealed interface ConversationAction {
    data class OnDraftChange(val text: String) : ConversationAction
    data object OnSend : ConversationAction
    data class OnRetry(val message: ChatMessage) : ConversationAction
    data object OnLoadOlder : ConversationAction
    data object OnReload : ConversationAction
    data class OnShowShareSheet(val show: Boolean) : ConversationAction
    data class OnShareResult(val result: TestResult) : ConversationAction
    data class OnReportRequest(val message: ChatMessage?) : ConversationAction
    data object OnConfirmReport : ConversationAction
    data class OnBlockRequest(val participant: ChatParticipant?) : ConversationAction
    data object OnConfirmBlock : ConversationAction
    data object OnToggleMute : ConversationAction
    data object OnLeaveCircle : ConversationAction
    data class OnMessagePrivately(val userId: Long) : ConversationAction
}

sealed interface ConversationEvent {
    data object Close : ConversationEvent
    data class OpenConversation(val id: Long) : ConversationEvent
}

@OptIn(ExperimentalUuidApi::class)
@HiltViewModel
class ConversationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val chatRepository: ChatRepository,
    private val database: AppDatabase,
) : ViewModel() {

    private val conversationId = savedStateHandle.toRoute<ConversationPage>().id

    private val _state = MutableStateFlow(ConversationState(conversationId = conversationId))
    val state: StateFlow<ConversationState> = _state.asStateFlow()

    private val _events = Channel<ConversationEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        chatRepository.connect()
        viewModelScope.launch {
            database.userDao().getUser().collect { user ->
                _state.update { it.copy(myUserId = user?.id) }
            }
        }
        viewModelScope.launch {
            database.testResultsDao().getTestResults().collect { results ->
                _state.update { it.copy(results = results) }
            }
        }
        viewModelScope.launch {
            chatRepository.events.collect { event ->
                when (event.type) {
                    "message" -> event.message?.takeIf { it.conversationId == conversationId }?.let { onIncoming(it) }
                    "error" -> onSocketError(event.error)
                }
            }
        }
        load()
    }

    fun onAction(action: ConversationAction) {
        when (action) {
            is ConversationAction.OnDraftChange -> _state.update { it.copy(draft = action.text) }
            ConversationAction.OnSend -> sendDraft()
            is ConversationAction.OnRetry -> retry(action.message)
            ConversationAction.OnLoadOlder -> loadOlder()
            ConversationAction.OnReload -> load()
            is ConversationAction.OnShowShareSheet -> _state.update { it.copy(showShareSheet = action.show) }
            is ConversationAction.OnShareResult -> shareResult(action.result)
            is ConversationAction.OnReportRequest -> _state.update { it.copy(reportTarget = action.message) }
            ConversationAction.OnConfirmReport -> confirmReport()
            is ConversationAction.OnBlockRequest -> _state.update { it.copy(blockTarget = action.participant) }
            ConversationAction.OnConfirmBlock -> confirmBlock()
            ConversationAction.OnToggleMute -> toggleMute()
            ConversationAction.OnLeaveCircle -> leaveCircle()
            is ConversationAction.OnMessagePrivately -> messagePrivately(action.userId)
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val conversationResult = chatRepository.getConversation(conversationId)
            val messagesResult = chatRepository.getMessages(conversationId)
            if (conversationResult.status != ResultStatus.SUCCESS || messagesResult.status != ResultStatus.SUCCESS) {
                _state.update {
                    it.copy(isLoading = false, error = conversationResult.message ?: messagesResult.message)
                }
                return@launch
            }
            val page = messagesResult.data.orEmpty()
            _state.update { current ->
                val pending = current.messages.filter { it.id == 0L }
                current.copy(
                    isLoading = false,
                    conversation = conversationResult.data,
                    messages = page.reversed() + pending,
                    hasMore = page.size >= PAGE_SIZE,
                )
            }
            chatRepository.markRead(conversationId)
        }
    }

    private fun loadOlder() {
        val current = _state.value
        if (current.isLoadingOlder || !current.hasMore) return
        val oldest = current.messages.firstOrNull { it.id > 0 }?.id ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoadingOlder = true) }
            val result = chatRepository.getMessages(conversationId, before = oldest)
            val page = result.data.orEmpty()
            _state.update {
                it.copy(
                    isLoadingOlder = false,
                    messages = if (result.status == ResultStatus.SUCCESS) page.reversed() + it.messages else it.messages,
                    hasMore = result.status == ResultStatus.SUCCESS && page.size >= PAGE_SIZE,
                )
            }
        }
    }

    private fun onIncoming(message: ChatMessage) {
        _state.update { current ->
            val withoutPending = current.messages.filterNot {
                it.id == 0L && it.clientId == message.clientId && it.senderId == message.senderId
            }
            if (withoutPending.any { it.id == message.id }) {
                current.copy(messages = withoutPending)
            } else {
                current.copy(messages = withoutPending + message)
            }
        }
        if (message.senderId != _state.value.myUserId) {
            viewModelScope.launch { chatRepository.markRead(conversationId) }
        }
    }

    private fun onSocketError(error: String?) {
        val pending = _state.value.messages.lastOrNull { it.pending } ?: return
        markFailed(pending.clientId)
        viewModelScope.launch {
            SnackBarViewModel.sendEvent(SnackBarItem(message = error ?: "Message not sent", isError = true))
        }
    }

    private fun sendDraft() {
        val text = _state.value.draft.trim()
        if (text.isEmpty()) return
        _state.update { it.copy(draft = "") }
        send(body = text, resultId = null, sharedResult = null)
    }

    private fun shareResult(result: TestResult) {
        _state.update { it.copy(showShareSheet = false) }
        send(
            body = "",
            resultId = result.id,
            sharedResult = SharedResult(
                id = result.id,
                results = result.results,
                partnerResults = result.partnerResults,
                status = result.status,
                date = result.date,
                image = result.image,
            ),
        )
    }

    private fun send(body: String, resultId: Long?, sharedResult: SharedResult?) {
        val myId = _state.value.myUserId ?: return
        val pending = ChatMessage(
            conversationId = conversationId,
            senderId = myId,
            clientId = Uuid.random().toString(),
            kind = if (resultId != null) MessageKind.RESULT else MessageKind.TEXT,
            body = body,
            resultId = resultId,
            result = sharedResult,
            createdAt = Clock.System.now().toString(),
            pending = true,
        )
        _state.update { it.copy(messages = it.messages + pending) }
        dispatch(pending)
    }

    private fun retry(message: ChatMessage) {
        _state.update { current ->
            current.copy(messages = current.messages.map {
                if (it.clientId == message.clientId && it.id == 0L) it.copy(pending = true, failed = false) else it
            })
        }
        dispatch(message)
    }

    private fun dispatch(message: ChatMessage) {
        viewModelScope.launch {
            val result = chatRepository.sendMessage(
                conversationId = conversationId,
                clientId = message.clientId,
                body = message.body,
                resultId = message.resultId,
            )
            if (result.status != ResultStatus.SUCCESS) {
                markFailed(message.clientId)
                SnackBarViewModel.sendEvent(SnackBarItem(message = result.message ?: "Message not sent", isError = true))
            }
        }
    }

    private fun markFailed(clientId: String) {
        _state.update { current ->
            current.copy(messages = current.messages.map {
                if (it.clientId == clientId && it.id == 0L) it.copy(pending = false, failed = true) else it
            })
        }
    }

    private fun confirmReport() {
        val target = _state.value.reportTarget ?: return
        _state.update { it.copy(reportTarget = null) }
        viewModelScope.launch {
            val result = chatRepository.reportMessage(target.id, reason = "")
            SnackBarViewModel.sendEvent(
                SnackBarItem(
                    message = result.data ?: result.message ?: "Something went wrong. Try again",
                    isError = result.status != ResultStatus.SUCCESS,
                )
            )
        }
    }

    private fun confirmBlock() {
        val target = _state.value.blockTarget ?: return
        _state.update { it.copy(blockTarget = null) }
        viewModelScope.launch {
            val result = chatRepository.blockUser(target.userId)
            if (result.status == ResultStatus.SUCCESS) {
                SnackBarViewModel.sendEvent(SnackBarItem(message = result.data ?: "User blocked"))
                if (_state.value.type != ConversationType.CIRCLE) {
                    _events.send(ConversationEvent.Close)
                } else {
                    _state.update { state -> state.copy(messages = state.messages.filterNot { it.senderId == target.userId }) }
                }
            } else {
                SnackBarViewModel.sendEvent(SnackBarItem(message = result.message ?: "Something went wrong. Try again", isError = true))
            }
        }
    }

    private fun toggleMute() {
        val conversation = _state.value.conversation ?: return
        val muted = !conversation.muted
        _state.update { it.copy(conversation = conversation.copy(muted = muted)) }
        viewModelScope.launch {
            val result = chatRepository.setMuted(conversationId, muted)
            if (result.status != ResultStatus.SUCCESS) {
                _state.update { it.copy(conversation = conversation) }
                SnackBarViewModel.sendEvent(SnackBarItem(message = result.message ?: "Something went wrong. Try again", isError = true))
            }
        }
    }

    private fun leaveCircle() {
        viewModelScope.launch {
            val result = chatRepository.leaveCircle(conversationId)
            if (result.status == ResultStatus.SUCCESS) {
                _events.send(ConversationEvent.Close)
            } else {
                SnackBarViewModel.sendEvent(SnackBarItem(message = result.message ?: "Something went wrong. Try again", isError = true))
            }
        }
    }

    private fun messagePrivately(userId: Long) {
        viewModelScope.launch {
            val result = chatRepository.startDirectChat(userId)
            val conversation = result.data
            if (result.status == ResultStatus.SUCCESS && conversation != null) {
                _events.send(ConversationEvent.OpenConversation(conversation.id))
            } else {
                SnackBarViewModel.sendEvent(SnackBarItem(message = result.message ?: "Something went wrong. Try again", isError = true))
            }
        }
    }

    private companion object {
        const val PAGE_SIZE = 50
    }
}
