package com.charlesmuchogo.research.presentation.authentication.forgotPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charlesmuchogo.research.data.remote.RemoteRepository
import com.charlesmuchogo.research.domain.dto.authentication.ForgotPasswordRequest
import com.charlesmuchogo.research.domain.models.SnackBarItem
import com.charlesmuchogo.research.domain.viewmodels.SnackBarViewModel
import com.charlesmuchogo.research.presentation.utils.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewmodel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val snackBarViewModel: SnackBarViewModel
) : ViewModel() {

    private val _state = MutableStateFlow(ForgotPasswordState())

    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = ForgotPasswordState()
    )


    fun onAction(action: ForgotPasswordAction) {
        viewModelScope.launch {
            when (action) {
                is ForgotPasswordAction.OnUpdateEmail -> {
                    _state.update { it.copy(email = action.email) }
                }

                ForgotPasswordAction.OnSubmit -> {

                    val email = state.value.email

                    val emailError = if (isValidEmail(email)) null else "Enter a valid email"

                    _state.update {
                        it.copy(
                            emailError = emailError
                        )
                    }

                    if(emailError != null){
                        return@launch
                    }
                    _state.update { it.copy(isSubmittingEmail = true) }

                    val response = remoteRepository.forgotPassword(ForgotPasswordRequest(email))

                    response.data?.let { data ->
                        snackBarViewModel.sendEvent(SnackBarItem(message = data.message))
                        _state.update { it.copy(isSubmittingEmail = false, hasSubmittedEmail = true) }
                    }

                    response.message?.let { msg ->
                        snackBarViewModel.sendEvent(SnackBarItem(isError = true, message = msg))
                        _state.update { it.copy(isSubmittingEmail = false, hasSubmittedEmail = false) }
                    }
                }
            }
        }
    }

}