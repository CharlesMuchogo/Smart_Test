package com.charlesmuchogo.research.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorDTO(
    val message: String,
)
