package com.charlesmuchogo.research.domain.dto.authentication

import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordRequest(
    val email: String
)
