package com.charlesmuchogo.research.presentation.bottomBar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charlesmuchogo.research.data.local.AppDatabase
import com.charlesmuchogo.research.data.remote.ChatRepository
import com.charlesmuchogo.research.presentation.utils.ResultStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomBarViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val database: AppDatabase,
): ViewModel() {

    private val _state = MutableStateFlow(BottomBarState())

    val state = combine(_state, database.testResultsDao().getTestResults()) { current, results ->
        current.copy(hasPendingResults = results.any { it.status.lowercase() == "pending" })
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = BottomBarState()
    )

    private var signedIn = false
    private var refreshJob: Job? = null

    init {
        viewModelScope.launch {
            database.userDao().getUser().map { it != null }.distinctUntilChanged().collect { isSignedIn ->
                signedIn = isSignedIn
                if (isSignedIn) {
                    chatRepository.connect()
                    refreshUnread()
                } else {
                    _state.update { it.copy(unreadMessages = 0) }
                }
            }
        }
        viewModelScope.launch {
            chatRepository.events.collect { event ->
                if (event.type == "message") {
                    refreshJob?.cancel()
                    refreshJob = launch {
                        delay(600)
                        refreshUnread()
                    }
                }
            }
        }
    }

    fun onAction(action: BottomBarAction){
        when(action){

            is BottomBarAction.OnUpdateSelectedItem -> {
                _state.update { it.copy(selectedBottomBarItem = action.item) }
                refreshUnread()
            }

            is BottomBarAction.OnUpdateSelectedLanguage -> {
                _state.update { it.copy(selectedKiswahiliLanguage = action.isKiswahili) }
            }

            BottomBarAction.OnRefreshUnread -> refreshUnread()
        }
    }

    private fun refreshUnread() {
        if (!signedIn) return
        viewModelScope.launch {
            val result = chatRepository.getConversations()
            if (result.status == ResultStatus.SUCCESS) {
                _state.update { state -> state.copy(unreadMessages = result.data.orEmpty().filterNot { it.muted }.sumOf { it.unread }) }
            }
        }
    }
}
