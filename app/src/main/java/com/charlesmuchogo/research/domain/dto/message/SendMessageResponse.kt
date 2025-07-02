package com.charlesmuchogo.research.domain.dto.message

import com.charlesmuchogo.research.domain.models.Message
import kotlinx.serialization.Serializable

@Serializable
data class SendMessageResponse(
    val message: String,
    val data: Message
)
