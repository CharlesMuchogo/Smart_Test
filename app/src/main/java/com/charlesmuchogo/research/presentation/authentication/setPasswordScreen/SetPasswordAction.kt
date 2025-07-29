package com.charlesmuchogo.research.presentation.authentication.setPasswordScreen

sealed interface SetPasswordAction {

    data object OnSubmit: SetPasswordAction

    data class OnUpdateToken(val token: String): SetPasswordAction

    data class OnUpdatePassword(val password: String): SetPasswordAction

    data class OnUpdateConfirmPassword(val password: String): SetPasswordAction

    data class OnShowPassword(val showPassword: Boolean): SetPasswordAction

}