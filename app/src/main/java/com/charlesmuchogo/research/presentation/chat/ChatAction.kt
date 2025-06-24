package com.charlesmuchogo.research.presentation.chat

sealed interface ChatAction {
    data class OnMessageChange(val message: String): ChatAction

    data object OnSubmitMessage: ChatAction
}