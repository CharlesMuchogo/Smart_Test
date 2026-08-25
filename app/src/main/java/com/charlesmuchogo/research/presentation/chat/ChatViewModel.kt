package com.charlesmuchogo.research.presentation.chat

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charlesmuchogo.research.analytics
import com.charlesmuchogo.research.data.local.AppDatabase
import com.charlesmuchogo.research.data.local.multiplatformSettings.MultiplatformSettingsRepository
import com.charlesmuchogo.research.data.remote.RemoteRepository
import com.charlesmuchogo.research.data.remote.WebsocketRepository
import com.charlesmuchogo.research.domain.models.Message
import com.charlesmuchogo.research.domain.models.SnackBarItem
import com.charlesmuchogo.research.domain.viewmodels.SnackBarViewModel
import com.charlesmuchogo.research.presentation.utils.formatChatDate
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val database: AppDatabase,
    private val websocketRepository: WebsocketRepository,
    private val remoteRepository: RemoteRepository,
    private val settingsRepository: MultiplatformSettingsRepository,
) : ViewModel() {

    init {
        viewModelScope.launch {
            websocketRepository.connectWebSocket()
        }
    }


    private val _state = MutableStateFlow(ChatState())
    val state = combine(
        _state,
        database.messagesDao().getMessages(),
        settingsRepository.getSubscriptionStatus(),
    ) { currentState, messages, subscribed ->
        val dateMessages =
            messages.groupBy { message ->
                val instant = Instant.fromEpochMilliseconds(message.timestamp)
                val date = instant.toLocalDateTime(TimeZone.currentSystemDefault())
                val currentDate =
                    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

                when (date.date) {
                    currentDate.date -> "Today"
                    currentDate.date.minus(1, DateTimeUnit.DAY) -> "Yesterday"
                    else -> formatChatDate(date.date)
                }
            }

        val sentToday = dateMessages["Today"]?.count { it.sender == MessageSender.ME.name } ?: 0

        currentState.copy(
            messages = dateMessages,
            isLoading = false,
            isSubscribed = subscribed == true,
            dailyMessageCount = sentToday,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = ChatState()
    )

    fun onAction(action: ChatAction) {
        viewModelScope.launch {
            when (action) {
                is ChatAction.OnMessageChange -> {
                    _state.update { it.copy(message = action.message) }
                }

                ChatAction.OnSubmitMessage -> {
                    val currentState = state.value
                    if (!currentState.isSubscribed && currentState.dailyMessageCount >= currentState.dailyMessageLimit) {
                        SnackBarViewModel.sendEvent(
                            SnackBarItem(
                                message = "You've reached today's free message limit. Subscribe for unlimited chat.",
                                isError = true
                            )
                        )
                        return@launch
                    }

                    _state.update {
                        it.copy(
                            message = "",
                            isGeneratingContent = true,
                            messagesSubmittedThisSession = it.messagesSubmittedThisSession + 1,
                        )
                    }

                    try {
                        if (state.value.message.isNotBlank()) {
                            val prompt = state.value.message
                            val timeNow = Clock.System.now().toEpochMilliseconds()
                            val message = Message(
                                id = timeNow,
                                message = prompt,
                                sender = MessageSender.ME.name,
                                timestamp = timeNow
                            )
                            database.messagesDao().insertMessage(message = message)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        _state.update { it.copy(isGeneratingContent = false) }
                    }
                }

                is ChatAction.OnSelectMessage -> {
                    val messages = _state.value.selectedMessages.toMutableList()

                    if (messages.contains(action.message)){
                        messages.remove(action.message)
                    }else{
                        messages.add(action.message)
                    }

                    _state.update { it.copy(selectedMessages = messages) }
                }

                ChatAction.OnReportMessages -> {
                    _state.update { it.copy(selectedMessages = emptyList()) }
                    SnackBarViewModel.sendEvent(SnackBarItem(message = "We'll look into these messages"))
                }

                is ChatAction.OnUpdateShowAd -> {
                    _state.update { it.copy(showAd = action.show) }
                }

                is ChatAction.FetchMessages -> {
                    val response = remoteRepository.getMessages()
                    response.data?.let {
                        database.messagesDao().insertMessages(it.data)
                    }
                }
            }
        }
    }
}

enum class MessageSender {
    AI, ME
}