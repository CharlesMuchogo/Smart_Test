package com.charlesmuchogo.research.domain.events

data class AuthenticationEvent(
    val logout: Boolean = false
)
