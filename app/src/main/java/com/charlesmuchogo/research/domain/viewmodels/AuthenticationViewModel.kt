package com.charlesmuchogo.research.domain.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charlesmuchogo.research.data.local.AppDatabase
import com.charlesmuchogo.research.data.remote.RemoteRepository
import com.charlesmuchogo.research.domain.actions.LoginAction
import com.charlesmuchogo.research.domain.dto.login.LoginRequestDTO
import com.charlesmuchogo.research.domain.dto.login.LoginResponseDTO
import com.charlesmuchogo.research.domain.dto.login.RegistrationRequestDTO
import com.charlesmuchogo.research.domain.dto.updateUser.UpdateUserDetailsDTO
import com.charlesmuchogo.research.domain.dto.updateUser.UpdateUserDetailsResponseDTO
import com.charlesmuchogo.research.domain.events.AuthenticationEvent
import com.charlesmuchogo.research.domain.models.User
import com.charlesmuchogo.research.domain.states.LoginState
import com.charlesmuchogo.research.presentation.utils.ResultStatus
import com.charlesmuchogo.research.presentation.utils.Results
import com.charlesmuchogo.research.presentation.utils.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel
@Inject
constructor(
    private val database: AppDatabase,
    private val remoteRepository: RemoteRepository,
) : ViewModel() {

    var loginPageState by mutableStateOf(LoginState())
        private set

    val loginStatus = MutableStateFlow(
        Results<LoginResponseDTO>(
            data = null,
            message = null,
            status = ResultStatus.INITIAL,
        ),
    )
    val registrationStatus = MutableStateFlow(
        Results<LoginResponseDTO>(
            data = null,
            message = null,
            status = ResultStatus.INITIAL,
        ),
    )

    val profileStatus =
        MutableStateFlow(
            Results<User>(
                data = null,
                message = null,
                status = ResultStatus.INITIAL,
            ),
        )


    val authenticationEventState = MutableStateFlow(
        Results(
            status = ResultStatus.INITIAL,
            data = AuthenticationEvent(),
            message = null
        )
    )
    val completeRegistrationState = MutableStateFlow(
        Results<UpdateUserDetailsResponseDTO>(
            status = ResultStatus.INITIAL,
            data = null,
            message = null
        )
    )

    fun updateAuthenticationEvent(authenticationEvent: AuthenticationEvent) {
        authenticationEventState.value = Results.initial()
        authenticationEventState.value = Results.success(authenticationEvent)
    }

    init {
        getCurrentUser()
    }

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnLogin -> {


                var emailError = if (loginPageState.email.isBlank()) "Email is required" else null

                if(loginPageState.email.isNotBlank() && !isValidEmail(loginPageState.email)) emailError = "Enter a valid email address"

                val passwordError = if (loginPageState.password.isBlank()) "Password is required" else null
                println("clicked on login $passwordError , $emailError")

                loginPageState = loginPageState.copy(
                    emailError = emailError,
                    passwordError = passwordError
                )

                if (emailError == null && passwordError == null) {
                    login(
                        loginRequestDTO = LoginRequestDTO(
                            email = loginPageState.email.lowercase().trim(),
                            password = loginPageState.password,
                        )
                    )
                }
            }

            is LoginAction.OnEmailChange -> {
                loginPageState = loginPageState.copy(email = action.email)
            }

            is LoginAction.OnPasswordChange -> {
                loginPageState = loginPageState.copy(password = action.password)
            }

            is LoginAction.OnShowPasswordChange -> {
                loginPageState = loginPageState.copy(showPassword = action.showPassword)
            }
            is LoginAction.OnRememberMeChange -> {
                loginPageState = loginPageState.copy(rememberMe = action.rememberMe)
            }

            is LoginAction.OnSignup -> {

            }
        }
    }

    fun login(loginRequestDTO: LoginRequestDTO) {
        viewModelScope.launch {
            loginStatus.value = Results.loading()
            remoteRepository.login(loginRequestDTO).collect { results ->
                results.data?.let { result ->
                    database.userDao().insertUser(user = result.user.copy(token = result.token))
                }
                loginStatus.value = results
            }
        }
    }
    fun register(registrationRequestDTO: RegistrationRequestDTO) {
        viewModelScope.launch {
            registrationStatus.value = Results.loading()
            remoteRepository.signUp(registrationRequestDTO).collect { results ->
                results.data?.let { result ->
                    database.userDao().insertUser(user = result.user.copy(token = result.token))
                }
                registrationStatus.value = results
            }
        }
    }

    fun updateUserDetails(details: UpdateUserDetailsDTO){
        viewModelScope.launch {
            completeRegistrationState.value = Results.loading()
          remoteRepository.completeRegistration(detailsDTO = details).collect{ results ->
              results.data?.let {
                  database.userDao().updateUser(user = it.user)
              }
              completeRegistrationState.value = results
          }
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            profileStatus.value = Results.loading()
            database
                .userDao()
                .getUser()
                .catch {
                    profileStatus.value = Results.error()
                }.collect {
                    profileStatus.value = Results.success(it)
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            database.userDao().deleteUsers()
            database.testResultsDao().deleteResults()
        }
    }
}
