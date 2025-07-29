package com.charlesmuchogo.research.presentation.authentication.setPasswordScreen

data class SetPasswordState(
    val token: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val hasUpdatedPassword: Boolean = false,
    val isSubmittingPassword: Boolean = false,
    val showPassword: Boolean = false
)
