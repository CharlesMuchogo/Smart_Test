package com.charlesmuchogo.research.domain.dto.authentication

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDTO(
    val email: String,
    val password: String,
    val deviceId: String? = null,
)
