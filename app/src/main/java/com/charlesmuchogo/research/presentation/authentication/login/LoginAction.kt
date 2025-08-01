package com.charlesmuchogo.research.presentation.authentication.login

sealed interface LoginAction {
    data object OnLogin : LoginAction

    data class OnShowPasswordChange(val showPassword: Boolean) : LoginAction

    data class OnRememberMeChange(val rememberMe: Boolean) : LoginAction

    data class OnEmailChange(val email: String) : LoginAction

    data class OnGoogleLoadingChange(val loading: Boolean) : LoginAction

    data class OnGoogleErrorChange(val error: String) : LoginAction

    data class OnGoogleLogin(val token: String) : LoginAction

    data class OnPasswordChange(val password: String) : LoginAction
}
