package com.charlesmuchogo.research.presentation.authentication.forgotPassword

data class ForgotPasswordState(
    val email: String = "",
    val emailError: String? = null,
    val isSubmittingEmail: Boolean = false,
    val hasSubmittedEmail: Boolean = false
)
