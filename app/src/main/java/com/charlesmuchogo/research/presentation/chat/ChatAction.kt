package com.charlesmuchogo.research.presentation.chat

import com.charlesmuchogo.research.domain.models.Message

sealed interface ChatAction {

    data class OnMessageChange(val message: String): ChatAction

    data class OnSelectMessage(val message: Message): ChatAction

    data class OnUpdateShowAd(val show: Boolean): ChatAction

    data object FetchMessages : ChatAction

    data object OnSubmitMessage: ChatAction

    data object OnReportMessages: ChatAction

}