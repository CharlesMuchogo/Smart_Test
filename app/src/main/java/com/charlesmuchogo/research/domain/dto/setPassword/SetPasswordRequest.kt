package com.charlesmuchogo.research.domain.dto.setPassword

import kotlinx.serialization.Serializable

@Serializable
data class SetPasswordRequest(
    val password: String,
    val token: String
)
