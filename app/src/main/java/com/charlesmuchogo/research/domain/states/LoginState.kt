package com.charlesmuchogo.research.domain.states

data class LoginState(
    val domain: String = "",
    val email: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val rememberMe: Boolean = true,

    //Errors
    val domainError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
)
