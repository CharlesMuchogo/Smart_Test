package com.charlesmuchogo.research.presentation.bottomBar

data class BottomBarState(
    val selectedKiswahiliLanguage: Boolean = false,
    val selectedBottomBarItem: BottomNavigationItem = BottomNavigationItem.bottomNavigationItems.first()
)
