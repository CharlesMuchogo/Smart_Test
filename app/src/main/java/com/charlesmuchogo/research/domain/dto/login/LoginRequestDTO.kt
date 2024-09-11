package com.charlesmuchogo.research.domain.dto.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDTO(
    val email: String,
    val password: String
)