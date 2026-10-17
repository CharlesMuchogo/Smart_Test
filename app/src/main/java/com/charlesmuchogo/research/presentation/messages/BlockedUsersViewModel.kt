package com.charlesmuchogo.research.presentation.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charlesmuchogo.research.data.remote.ChatRepository
import com.charlesmuchogo.research.domain.models.SnackBarItem
import com.charlesmuchogo.research.domain.models.chat.BlockedUser
import com.charlesmuchogo.research.domain.viewmodels.SnackBarViewModel
import com.charlesmuchogo.research.presentation.utils.ResultStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BlockedUsersState(
    val users: List<BlockedUser> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val unblockTarget: BlockedUser? = null,
    val unblockingId: Long? = null,
)

sealed interface BlockedUsersAction {
    data object Load : BlockedUsersAction
    data class OnUnblockRequest(val user: BlockedUser?) : BlockedUsersAction
    data object OnConfirmUnblock : BlockedUsersAction
}

@HiltViewModel
class BlockedUsersViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(BlockedUsersState())
    val state: StateFlow<BlockedUsersState> = _state.asStateFlow()

    init {
        load()
    }

    fun onAction(action: BlockedUsersAction) {
        when (action) {
            BlockedUsersAction.Load -> load()
            is BlockedUsersAction.OnUnblockRequest -> _state.update { it.copy(unblockTarget = action.user) }
            BlockedUsersAction.OnConfirmUnblock -> unblock()
        }
    }

    private fun load() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = chatRepository.getBlockedUsers()
            _state.update {
                if (result.status == ResultStatus.SUCCESS) {
                    it.copy(users = result.data.orEmpty(), isLoading = false)
                } else {
                    it.copy(isLoading = false, error = result.message)
                }
            }
        }
    }

    private fun unblock() {
        val target = _state.value.unblockTarget ?: return
        _state.update { it.copy(unblockTarget = null, unblockingId = target.userId) }
        viewModelScope.launch {
            val result = chatRepository.unblockUser(target.userId)
            if (result.status == ResultStatus.SUCCESS) {
                _state.update { state ->
                    state.copy(users = state.users.filterNot { it.userId == target.userId }, unblockingId = null)
                }
                SnackBarViewModel.sendEvent(SnackBarItem(message = result.data ?: "User unblocked"))
            } else {
                _state.update { it.copy(unblockingId = null) }
                SnackBarViewModel.sendEvent(SnackBarItem(message = result.message ?: "Something went wrong. Try again", isError = true))
            }
        }
    }
}
