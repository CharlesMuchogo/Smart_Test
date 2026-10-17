package com.charlesmuchogo.research.presentation.bottomBar

data class BottomBarState(
    val selectedKiswahiliLanguage: Boolean = false,
    val selectedBottomBarItem: Int = 0,
    val unreadMessages: Int = 0,
    val hasPendingResults: Boolean = false,
)
