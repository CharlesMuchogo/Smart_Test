package com.charlesmuchogo.research.presentation.authentication.setPasswordScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charlesmuchogo.research.data.local.AppDatabase
import com.charlesmuchogo.research.data.local.multiplatformSettings.MultiplatformSettingsRepository
import com.charlesmuchogo.research.data.remote.RemoteRepository
import com.charlesmuchogo.research.domain.dto.setPassword.SetPasswordRequest
import com.charlesmuchogo.research.domain.models.SnackBarItem
import com.charlesmuchogo.research.domain.viewmodels.SnackBarViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetPasswordViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val settingsRepository: MultiplatformSettingsRepository,
    private val snackBarViewModel: SnackBarViewModel,
    private val database: AppDatabase
) : ViewModel() {

    private val _state = MutableStateFlow(SetPasswordState())

    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = SetPasswordState()
    )

    fun onAction(action: SetPasswordAction) {
        viewModelScope.launch {
            when (action) {

                is SetPasswordAction.OnUpdateConfirmPassword -> {
                    _state.update { it.copy(confirmPassword = action.password) }
                }

                is SetPasswordAction.OnUpdatePassword -> {
                    _state.update { it.copy(password = action.password) }
                }

                is SetPasswordAction.OnUpdateToken -> {
                    _state.update { it.copy(token = action.token) }
                }

                is SetPasswordAction.OnShowPassword -> {
                    _state.update { it.copy(showPassword = action.showPassword) }
                }

                SetPasswordAction.OnSubmit -> {
                    val password = _state.value.password
                    val confirmPassword = _state.value.confirmPassword
                    val token = _state.value.token

                    if (password.isBlank()) {
                        _state.update { it.copy(passwordError = "Password cannot be empty") }
                        return@launch
                    }

                    if (password != confirmPassword) {
                        _state.update {
                            it.copy(
                                confirmPasswordError = "Passwords do not match",
                                passwordError = "Passwords do not match"
                            )
                        }
                        return@launch
                    }

                    _state.update { it.copy(isSubmittingPassword = true, confirmPasswordError = null, passwordError = null) }

                    val response = remoteRepository.setPassword(
                        request = SetPasswordRequest(password = password, token = token)
                    )

                    response.data?.let {  data ->
                        database.userDao().insertUser(data.user)
                        settingsRepository.saveAccessToken(data.token)
                        snackBarViewModel.sendEvent(SnackBarItem(message = data.message))
                        _state.update { it.copy(hasUpdatedPassword = true, isSubmittingPassword = false) }
                    }

                    response.message?.let { msg ->
                        snackBarViewModel.sendEvent(SnackBarItem(message = msg, isError = true))
                        _state.update { it.copy(hasUpdatedPassword = false, isSubmittingPassword = false) }
                    }
                }
            }
        }
    }
}