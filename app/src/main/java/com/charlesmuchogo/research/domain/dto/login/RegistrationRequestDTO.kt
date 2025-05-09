package com.charlesmuchogo.research.domain.dto.login

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationRequestDTO(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val password: String,
    val country: String
)
