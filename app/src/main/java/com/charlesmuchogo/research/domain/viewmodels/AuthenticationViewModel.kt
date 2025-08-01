package com.charlesmuchogo.research.domain.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charlesmuchogo.research.data.local.AppDatabase
import com.charlesmuchogo.research.data.local.multiplatformSettings.MultiplatformSettingsRepository
import com.charlesmuchogo.research.data.remote.RemoteRepository
import com.charlesmuchogo.research.domain.actions.LoginAction
import com.charlesmuchogo.research.domain.dto.authentication.LoginRequestDTO
import com.charlesmuchogo.research.domain.dto.authentication.LoginResponseDTO
import com.charlesmuchogo.research.domain.dto.authentication.RegistrationRequestDTO
import com.charlesmuchogo.research.domain.dto.updateUser.UpdateUserDetailsDTO
import com.charlesmuchogo.research.domain.dto.updateUser.UpdateUserDetailsResponseDTO
import com.charlesmuchogo.research.domain.models.SnackBarItem
import com.charlesmuchogo.research.domain.models.User
import com.charlesmuchogo.research.domain.states.LoginState
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.navigation.RegistrationPage
import com.charlesmuchogo.research.presentation.utils.ResultStatus
import com.charlesmuchogo.research.presentation.utils.Results
import com.charlesmuchogo.research.presentation.utils.isValidDate
import com.charlesmuchogo.research.presentation.utils.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel
@Inject
constructor(
    private val database: AppDatabase,
    private val settingsRepository: MultiplatformSettingsRepository,
    private val snackBarViewModel: SnackBarViewModel,
    private val remoteRepository: RemoteRepository,
) : ViewModel() {

    var loginPageState by mutableStateOf(LoginState())
        private set



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

    val completeRegistrationState = MutableStateFlow(
        Results<UpdateUserDetailsResponseDTO>(
            status = ResultStatus.INITIAL,
            data = null,
            message = null
        )
    )


    init {
        getCurrentUser()
    }

    fun onAction(action: LoginAction) {
        when (action) {


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

            is LoginAction.OnTermsAndConditionsChange -> {
                loginPageState = loginPageState.copy(termsAndConditions = action.agree)
            }

            is LoginAction.OnAgeChange -> {
                loginPageState = loginPageState.copy(age = action.value)
            }

            is LoginAction.OnConfirmPasswordChange -> {
                loginPageState = loginPageState.copy(confirmPassword = action.value)
            }

            is LoginAction.OnEducationLevelChange -> {
                loginPageState = loginPageState.copy(educationLevel = action.value)
            }

            is LoginAction.OnFirstNameChange -> {
                loginPageState = loginPageState.copy(firstName = action.value)
            }

            is LoginAction.OnGenderChange -> {
                loginPageState = loginPageState.copy(gender = action.value)
            }

            is LoginAction.OnHasTestedBeforeChange -> {
                loginPageState = loginPageState.copy(hasTestedBefore = action.value)
            }

            is LoginAction.OnLastNameChange -> {
                loginPageState = loginPageState.copy(lastname = action.value)
            }

            is LoginAction.OnMaritalStatusChange -> {
                loginPageState = loginPageState.copy(maritalStatus = action.value)
            }

            is LoginAction.OnPhoneNumberChange -> {
                loginPageState = loginPageState.copy(phoneNumber = action.value)
            }

            LoginAction.OnUpdateDetails -> {

                loginPageState = loginPageState.copy(
                    ageError = if (loginPageState.age.isBlank()) "Select your date of birth" else null,
                    educationLevelError = if (loginPageState.educationLevel.isBlank()) "Select your education level" else null,
                    genderError = if (loginPageState.gender.isBlank()) "Select your gender" else null,
                )

                if (loginPageState.age.isNotBlank()) {
                    loginPageState = loginPageState.copy(
                        ageError = if (!isValidDate(loginPageState.age)) "Invalid date of birth" else null
                    )
                }

                if (loginPageState.ageError != null || loginPageState.genderError != null || loginPageState.educationLevelError != null) {
                    return
                }

                updateUserDetails(
                    details = UpdateUserDetailsDTO(
                        testedBefore = loginPageState.hasTestedBefore,
                        educationLevel = loginPageState.educationLevel,
                        gender = loginPageState.gender,
                        age = loginPageState.age,
                        country = loginPageState.country
                    )
                )

            }


            is LoginAction.OnSignup -> {
                val firstNameError =
                    if (loginPageState.firstName.isBlank()) "Name is required" else null
                val lastNameError =
                    if (loginPageState.lastname.isBlank()) "Name is required" else null
                val phoneNumberError =
                    if (loginPageState.phoneNumber.isBlank()) "Phone number is required" else null
                var emailError = if (loginPageState.email.isBlank()) "Email is required" else null

                if (loginPageState.email.isNotBlank() && !isValidEmail(loginPageState.email)) emailError =
                    "Enter a valid email address"

                var passwordError =
                    if (loginPageState.password.isBlank()) "Password is required" else null
                var confirmPasswordError =
                    if (loginPageState.password.isBlank()) "Password is required" else null


                if (loginPageState.password != loginPageState.confirmPassword) {
                    passwordError = "Passwords do not match"
                    confirmPasswordError = "Passwords do not match"
                }

                loginPageState = loginPageState.copy(
                    firstNameError = firstNameError,
                    lastnameError = lastNameError,
                    phoneNumberError = phoneNumberError,
                    emailError = emailError,
                    passwordError = passwordError,
                    confirmPasswordError = confirmPasswordError
                )

                if (firstNameError == null && lastNameError == null && phoneNumberError == null && emailError == null && passwordError == null && confirmPasswordError == null) {

                    register(
                        registrationRequestDTO = RegistrationRequestDTO(
                            firstName = loginPageState.firstName,
                            lastName = loginPageState.lastname,
                            phone = loginPageState.phoneNumber,
                            email = loginPageState.email,
                            password = loginPageState.password,
                            country = loginPageState.country
                        ),
                    )
                }


            }

            is LoginAction.OnLogin -> {

                val emailError = if (loginPageState.email.isBlank()) "Email is required" else null

                val passwordError =
                    if (loginPageState.password.isBlank()) "Password is required" else null

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

            is LoginAction.OnCountryChange -> {
                loginPageState = loginPageState.copy(country = action.country)
            }
        }
    }



    private fun login(loginRequestDTO: LoginRequestDTO) {

    }

    private fun register(registrationRequestDTO: RegistrationRequestDTO) {
        viewModelScope.launch {
            registrationStatus.value = Results.loading()
            remoteRepository.signUp(registrationRequestDTO).collect { results ->
                results.data?.let { result ->
                    database.userDao().insertUser(user = result.user.copy(token = result.token))
                    navController.popBackStack(RegistrationPage, inclusive = true)
                }
                registrationStatus.value = results
            }
        }
    }

    private fun updateUserDetails(details: UpdateUserDetailsDTO) {
        viewModelScope.launch {
            completeRegistrationState.value = Results.loading()
            remoteRepository.completeRegistration(detailsDTO = details).collect { results ->
                results.data?.user?.let { user ->
                    val loggedInUser = database.userDao().getUser().firstOrNull()
                    loggedInUser?.let {
                        database.userDao().updateUser(user = user.copy(token = loggedInUser.token))
                    }
                }

                results.message?.let {
                    snackBarViewModel.sendEvent(SnackBarItem(message = it, isError = true))
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

    fun updateUser(user: User) {
        viewModelScope.launch {
            database.userDao().updateUser(user)
        }
    }

    fun logout() {
        viewModelScope.launch {
            database.userDao().deleteUsers()
            database.testResultsDao().deleteResults()
            database.messagesDao().deleteMessages()
            settingsRepository.clearSettings()
        }
    }


    val appTheme: StateFlow<Int?> =
        settingsRepository.getAppTheme().map { it }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 500L),
            initialValue = null,
        )

    val isFirstTime: StateFlow<Boolean?> =
        settingsRepository.getFirstTime().map { it }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 500L),
            initialValue = null,
        )

    fun updateAppTheme(theme: Boolean){
        settingsRepository.saveAppTheme(if (theme) 1 else 0)
    }
}
