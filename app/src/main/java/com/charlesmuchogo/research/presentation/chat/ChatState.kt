package com.charlesmuchogo.research.presentation.chat

import com.charlesmuchogo.research.domain.models.Message

const val FREE_DAILY_MESSAGES = 5
const val REWARDED_BONUS_MESSAGES = 5

data class ChatState(
    val message: String = "",
    val messages: Map<String, List<Message>> = mapOf(),
    val selectedMessages: List<Message> = listOf(),
    val isGeneratingContent: Boolean = false,
    val isLoading: Boolean = true,
    val isSubscribed: Boolean = false,
    val dailyMessageCount: Int = 0,
    val dailyMessageLimit: Int = FREE_DAILY_MESSAGES,
    val bonusMessages: Int = 0,
    val messagesSubmittedThisSession: Int = 0,
)