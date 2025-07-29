package com.charlesmuchogo.research.domain.dto.authentication

import com.charlesmuchogo.research.domain.models.User
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDTO(
    val message: String,
    val token: String,
    val user: User,
)
