package com.charlesmuchogo.research.domain.dto.updateUser

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserDetailsDTO(
    val testedBefore: Boolean,
    val educationLevel: String,
    val gender: String,
    val age: String,
    val country: String
)
