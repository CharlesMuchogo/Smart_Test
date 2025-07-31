package com.charlesmuchogo.research.domain.dto.authentication

import kotlinx.serialization.Serializable

@Serializable
data class GoogleLoginRequest(
    val token : String,
    val device_id : String,
    val country : String
)
