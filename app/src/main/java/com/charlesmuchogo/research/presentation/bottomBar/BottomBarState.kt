package com.charlesmuchogo.research.presentation.bottomBar

data class BottomBarState(
    val selectedKiswahiliLanguage: Boolean = false,
    val showAd: Boolean = true,
    val selectedBottomBarItem: BottomNavigationItem = BottomNavigationItem.bottomNavigationItems.first()
)
