package com.charlesmuchogo.research.presentation.bottomBar

sealed interface BottomBarAction {

    data class OnUpdateSelectedItem(val item: Int): BottomBarAction

    data class OnUpdateSelectedLanguage(val isKiswahili: Boolean): BottomBarAction

    data object OnRefreshUnread: BottomBarAction
}
