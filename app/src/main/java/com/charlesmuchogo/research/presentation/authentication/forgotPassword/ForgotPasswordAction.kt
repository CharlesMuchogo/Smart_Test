package com.charlesmuchogo.research.presentation.authentication.forgotPassword

sealed interface ForgotPasswordAction {

    data object OnSubmit: ForgotPasswordAction

    data class OnUpdateEmail(val email: String): ForgotPasswordAction

}