package com.charlesmuchogo.research.presentation.chat

import com.charlesmuchogo.research.domain.models.Message

data class ChatState(
    val message: String = "",
    val messages: Map<String, List<Message>> = mapOf(),
    val selectedMessages: List<Message> = listOf(),
    val isGeneratingContent: Boolean = false,
    val isLoading: Boolean = true,
    val showAd: Boolean = true
)