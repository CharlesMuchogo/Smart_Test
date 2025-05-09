package com.charlesmuchogo.research.domain.actions

sealed interface LoginAction {
    data object OnLogin : LoginAction
    data object OnSignup : LoginAction
    data object OnUpdateDetails : LoginAction
    data class OnShowPasswordChange(val showPassword: Boolean) : LoginAction
    data class OnRememberMeChange(val rememberMe: Boolean) : LoginAction
    data class OnTermsAndConditionsChange(val agree: Boolean) : LoginAction
    data class OnEmailChange(val email: String) : LoginAction
    data class OnCountryChange(val country: String) : LoginAction
    data class OnPasswordChange(val password: String) : LoginAction

    data class OnFirstNameChange(val value: String) : LoginAction
    data class OnLastNameChange(val value: String) : LoginAction
    data class OnPhoneNumberChange(val value: String) : LoginAction
    data class OnAgeChange(val value: String) : LoginAction
    data class OnGenderChange(val value: String) : LoginAction
    data class OnEducationLevelChange(val value: String) : LoginAction
    data class OnMaritalStatusChange(val value: String) : LoginAction
    data class OnConfirmPasswordChange(val value: String) : LoginAction
    data class OnHasTestedBeforeChange(val value: Boolean) : LoginAction
}
