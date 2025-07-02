package com.charlesmuchogo.research.presentation.authentication.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charlesmuchogo.research.data.local.AppDatabase
import com.charlesmuchogo.research.data.local.multiplatformSettings.MultiplatformSettingsRepository
import com.charlesmuchogo.research.data.remote.RemoteRepository
import com.charlesmuchogo.research.domain.dto.login.LoginRequestDTO
import com.charlesmuchogo.research.domain.models.SnackBarItem
import com.charlesmuchogo.research.domain.states.LoginState
import com.charlesmuchogo.research.domain.viewmodels.SnackBarViewModel
import com.charlesmuchogo.research.presentation.utils.getFcmToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject
constructor(
    private val database: AppDatabase,
    private val settingsRepository: MultiplatformSettingsRepository,
    private val snackBarViewModel: SnackBarViewModel,
    private val remoteRepository: RemoteRepository,
) : ViewModel() {


    private val _state = MutableStateFlow(LoginState())

    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = LoginState()
    )

    fun onAction(action: LoginAction) {
        viewModelScope.launch {
            when (action) {
                is LoginAction.OnEmailChange -> {
                    _state.update { it.copy(email = action.email) }
                }

                is LoginAction.OnPasswordChange -> {
                    _state.update { it.copy(password = action.password) }
                }

                is LoginAction.OnRememberMeChange -> {
                    _state.update { it.copy(rememberMe = action.rememberMe) }
                }

                is LoginAction.OnShowPasswordChange -> {
                    _state.update { it.copy(showPassword = action.showPassword) }
                }

                LoginAction.OnLogin -> {
                    _state.update { it.copy(isLoggingIn = true) }

                    val emailError = if (_state.value.email.isBlank()) "Email is required" else null

                    val passwordError =
                        if (_state.value.password.isBlank()) "Password is required" else null

                    _state.update {
                        it.copy(
                            emailError = emailError,
                            passwordError = passwordError
                        )
                    }

                    if (emailError == null && passwordError == null) {
                        _state.update { it.copy(isLoggingIn = true) }
                        val loginRequestDTO = LoginRequestDTO(
                            email = _state.value.email,
                            password = _state.value.password,
                            deviceId = getFcmToken()
                        )

                        val response = remoteRepository.login(loginRequestDTO)

                        response.data?.let { result ->
                            database.userDao()
                                .insertUser(user = result.user.copy(token = result.token))
                            settingsRepository.saveAccessToken(result.token)
                            _state.update { it.copy(isLoggingIn = false, hasLoggedIn = true, loggedInUser = result.user) }
                        }


                        response.message?.let {
                            snackBarViewModel.sendEvent(SnackBarItem(message = it, isError = true))
                            _state.update { it.copy(isLoggingIn = false, hasLoggedIn = false) }
                        }

                    }
                }
            }
        }
    }
}