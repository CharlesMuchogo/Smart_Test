package com.charlesmuchogo.research.domain.dto.updateUser

import com.charlesmuchogo.research.domain.models.User
import kotlinx.serialization.Serializable


@Serializable
data class UpdateUserDetailsResponseDTO(
    val message: String,
    val user: User
)
