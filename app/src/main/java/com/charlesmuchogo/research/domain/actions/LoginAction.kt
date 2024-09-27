package com.charlesmuchogo.research.domain.actions

sealed interface LoginAction {
    data object OnLogin : LoginAction
    data object OnSignup : LoginAction
    data class OnEmailChange(val email: String) : LoginAction
    data class OnPasswordChange(val password: String) : LoginAction
    data class OnShowPasswordChange(val showPassword: Boolean) : LoginAction
    data class OnRememberMeChange(val rememberMe: Boolean) : LoginAction
}