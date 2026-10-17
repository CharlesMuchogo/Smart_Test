package com.charlesmuchogo.research.presentation.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charlesmuchogo.research.data.remote.ChatRepository
import com.charlesmuchogo.research.domain.models.SnackBarItem
import com.charlesmuchogo.research.domain.models.chat.Doctor
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

data class DoctorsState(
    val isLoading: Boolean = true,
    val doctors: List<Doctor> = emptyList(),
    val selectedSpecialty: String? = null,
    val startingDoctorId: Long? = null,
    val error: String? = null,
) {
    val specialties: List<String>
        get() = doctors.map { it.specialty }.filter { it.isNotBlank() }.distinct()

    val visibleDoctors: List<Doctor>
        get() = selectedSpecialty?.let { specialty -> doctors.filter { it.specialty == specialty } } ?: doctors
}

sealed interface DoctorsAction {
    data object Load : DoctorsAction
    data class OnSpecialtySelected(val specialty: String?) : DoctorsAction
    data class OnStartChat(val doctor: Doctor) : DoctorsAction
}

@HiltViewModel
class DoctorsViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(DoctorsState())
    val state: StateFlow<DoctorsState> = _state.asStateFlow()

    private val _openConversation = Channel<Long>(Channel.BUFFERED)
    val openConversation = _openConversation.receiveAsFlow()

    init {
        load()
    }

    fun onAction(action: DoctorsAction) {
        when (action) {
            DoctorsAction.Load -> load()
            is DoctorsAction.OnSpecialtySelected -> _state.update { it.copy(selectedSpecialty = action.specialty) }
            is DoctorsAction.OnStartChat -> startChat(action.doctor)
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = chatRepository.getDoctors()
            _state.update {
                if (result.status == ResultStatus.SUCCESS) {
                    it.copy(isLoading = false, doctors = result.data.orEmpty())
                } else {
                    it.copy(isLoading = false, error = result.message)
                }
            }
        }
    }

    private fun startChat(doctor: Doctor) {
        if (_state.value.startingDoctorId != null) return
        viewModelScope.launch {
            _state.update { it.copy(startingDoctorId = doctor.userId) }
            val result = chatRepository.startDoctorChat(doctor.userId)
            _state.update { it.copy(startingDoctorId = null) }
            val conversation = result.data
            if (result.status == ResultStatus.SUCCESS && conversation != null) {
                _openConversation.send(conversation.id)
            } else {
                SnackBarViewModel.sendEvent(SnackBarItem(message = result.message ?: "Something went wrong. Try again", isError = true))
            }
        }
    }
}
