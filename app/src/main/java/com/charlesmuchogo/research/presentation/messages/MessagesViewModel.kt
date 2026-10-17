package com.charlesmuchogo.research.presentation.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charlesmuchogo.research.data.remote.ChatRepository
import com.charlesmuchogo.research.domain.models.chat.Conversation
import com.charlesmuchogo.research.domain.models.chat.ConversationType
import com.charlesmuchogo.research.presentation.utils.ResultStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class InboxFilter { ALL, DOCTORS, COMMUNITY }

data class MessagesState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val conversations: List<Conversation> = emptyList(),
    val filter: InboxFilter = InboxFilter.ALL,
    val error: String? = null,
) {
    val visibleConversations: List<Conversation>
        get() = when (filter) {
            InboxFilter.ALL -> conversations
            InboxFilter.DOCTORS -> conversations.filter { it.type == ConversationType.DOCTOR }
            InboxFilter.COMMUNITY -> conversations.filter { it.type != ConversationType.DOCTOR }
        }
}

sealed interface MessagesAction {
    data object Refresh : MessagesAction
    data class OnFilterChange(val filter: InboxFilter) : MessagesAction
}

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MessagesState())
    val state: StateFlow<MessagesState> = _state.asStateFlow()

    private var refreshJob: Job? = null

    init {
        chatRepository.connect()
        viewModelScope.launch {
            chatRepository.events.collect { event ->
                if (event.type == "message") scheduleRefresh()
            }
        }
    }

    fun onAction(action: MessagesAction) {
        when (action) {
            MessagesAction.Refresh -> load(showRefreshing = _state.value.conversations.isNotEmpty())
            is MessagesAction.OnFilterChange -> _state.update { it.copy(filter = action.filter) }
        }
    }

    private fun scheduleRefresh() {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            delay(400)
            load(showRefreshing = false)
        }
    }

    private fun load(showRefreshing: Boolean) {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = showRefreshing, error = null) }
            val result = chatRepository.getConversations()
            _state.update {
                if (result.status == ResultStatus.SUCCESS) {
                    it.copy(isLoading = false, isRefreshing = false, conversations = result.data.orEmpty())
                } else {
                    it.copy(isLoading = false, isRefreshing = false, error = result.message)
                }
            }
        }
    }
}
