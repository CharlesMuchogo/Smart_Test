package com.charlesmuchogo.research.presentation.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charlesmuchogo.research.data.remote.ChatRepository
import com.charlesmuchogo.research.domain.models.SnackBarItem
import com.charlesmuchogo.research.domain.models.chat.Circle
import com.charlesmuchogo.research.domain.models.chat.CommunityProfile
import com.charlesmuchogo.research.domain.models.chat.MessagePrivacy
import com.charlesmuchogo.research.domain.viewmodels.SnackBarViewModel
import com.charlesmuchogo.research.presentation.utils.ResultStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CommunityState(
    val isLoading: Boolean = true,
    val circles: List<Circle> = emptyList(),
    val profile: CommunityProfile? = null,
    val joiningCircleId: Long? = null,
    val isMatching: Boolean = false,
    val showIdentitySheet: Boolean = false,
    val aliasDraft: String = "",
    val privacyDraft: String = MessagePrivacy.CIRCLES,
    val isSavingProfile: Boolean = false,
    val profileError: String? = null,
    val error: String? = null,
)

sealed interface CommunityAction {
    data object Load : CommunityAction
    data class OnCircleClick(val circle: Circle) : CommunityAction
    data object OnFindBuddy : CommunityAction
    data class OnShowIdentitySheet(val show: Boolean) : CommunityAction
    data class OnAliasChange(val alias: String) : CommunityAction
    data class OnPrivacyChange(val privacy: String) : CommunityAction
    data object OnSaveIdentity : CommunityAction
}

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CommunityState())
    val state: StateFlow<CommunityState> = _state.asStateFlow()

    private val _openConversation = Channel<Long>(Channel.BUFFERED)
    val openConversation = _openConversation.receiveAsFlow()

    init {
        load()
    }

    fun onAction(action: CommunityAction) {
        when (action) {
            CommunityAction.Load -> load()
            is CommunityAction.OnCircleClick -> openCircle(action.circle)
            CommunityAction.OnFindBuddy -> findBuddy()
            is CommunityAction.OnShowIdentitySheet -> _state.update {
                it.copy(
                    showIdentitySheet = action.show,
                    aliasDraft = it.profile?.alias.orEmpty(),
                    privacyDraft = it.profile?.messagePrivacy ?: MessagePrivacy.CIRCLES,
                    profileError = null,
                )
            }
            is CommunityAction.OnAliasChange -> _state.update { it.copy(aliasDraft = action.alias.take(20), profileError = null) }
            is CommunityAction.OnPrivacyChange -> _state.update { it.copy(privacyDraft = action.privacy) }
            CommunityAction.OnSaveIdentity -> saveIdentity()
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val circles = chatRepository.getCircles()
            val profile = chatRepository.getCommunityProfile()
            _state.update {
                it.copy(
                    isLoading = false,
                    circles = circles.data ?: it.circles,
                    profile = profile.data ?: it.profile,
                    error = if (circles.status == ResultStatus.SUCCESS) null else circles.message,
                )
            }
        }
    }

    private fun openCircle(circle: Circle) {
        if (circle.joined) {
            viewModelScope.launch { _openConversation.send(circle.id) }
            return
        }
        if (_state.value.joiningCircleId != null) return
        viewModelScope.launch {
            _state.update { it.copy(joiningCircleId = circle.id) }
            val result = chatRepository.joinCircle(circle.id)
            _state.update { it.copy(joiningCircleId = null, circles = result.data ?: it.circles) }
            if (result.status == ResultStatus.SUCCESS) {
                _openConversation.send(circle.id)
            } else {
                SnackBarViewModel.sendEvent(SnackBarItem(message = result.message ?: "Something went wrong. Try again", isError = true))
            }
        }
    }

    private fun findBuddy() {
        if (_state.value.isMatching) return
        viewModelScope.launch {
            _state.update { it.copy(isMatching = true) }
            val result = chatRepository.matchPeer()
            _state.update { it.copy(isMatching = false) }
            val conversation = result.data
            if (result.status == ResultStatus.SUCCESS && conversation != null) {
                _openConversation.send(conversation.id)
            } else {
                SnackBarViewModel.sendEvent(SnackBarItem(message = result.message ?: "Something went wrong. Try again", isError = true))
            }
        }
    }

    private fun saveIdentity() {
        val current = _state.value
        if (current.isSavingProfile) return
        viewModelScope.launch {
            _state.update { it.copy(isSavingProfile = true, profileError = null) }
            val result = chatRepository.updateCommunityProfile(current.aliasDraft.trim(), current.privacyDraft)
            _state.update {
                if (result.status == ResultStatus.SUCCESS) {
                    it.copy(isSavingProfile = false, profile = result.data, showIdentitySheet = false)
                } else {
                    it.copy(isSavingProfile = false, profileError = result.message)
                }
            }
        }
    }
}
