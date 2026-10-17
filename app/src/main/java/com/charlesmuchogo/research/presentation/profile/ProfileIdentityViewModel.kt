package com.charlesmuchogo.research.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charlesmuchogo.research.data.remote.ChatRepository
import com.charlesmuchogo.research.domain.models.chat.CommunityProfile
import com.charlesmuchogo.research.presentation.utils.ResultStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileIdentityState(
    val profile: CommunityProfile? = null,
    val isSaving: Boolean = false,
)

@HiltViewModel
class ProfileIdentityViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileIdentityState())
    val state: StateFlow<ProfileIdentityState> = _state.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            val result = chatRepository.getCommunityProfile()
            if (result.status == ResultStatus.SUCCESS) {
                _state.update { it.copy(profile = result.data) }
            }
        }
    }

    fun updatePrivacy(privacy: String) {
        val current = _state.value.profile ?: return
        if (current.messagePrivacy == privacy || _state.value.isSaving) return
        _state.update { it.copy(profile = current.copy(messagePrivacy = privacy), isSaving = true) }
        viewModelScope.launch {
            val result = chatRepository.updateCommunityProfile(current.alias, privacy)
            _state.update {
                it.copy(
                    profile = if (result.status == ResultStatus.SUCCESS) result.data ?: it.profile else current,
                    isSaving = false,
                )
            }
        }
    }
}
