package com.charlesmuchogo.research.domain.dto.message

import com.charlesmuchogo.research.domain.models.Message
import kotlinx.serialization.Serializable

@Serializable
data class GetMessagesDTO(
    val message: String,
    val data: List<Message>
)
