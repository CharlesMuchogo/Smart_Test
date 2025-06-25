package com.charlesmuchogo.research.presentation.chat

import com.charlesmuchogo.research.domain.models.Message

data class ChatState(
    val message: String = "",
    val messages: Map<String, List<Message>> = mapOf(),
    val isGeneratingContent: Boolean = false
)