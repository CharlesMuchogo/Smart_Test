package com.charlesmuchogo.research.presentation.bottomBar

sealed interface BottomBarAction {

    data class OnUpdateSelectedItem(val item: BottomNavigationItem): BottomBarAction

    data class OnUpdateSelectedLanguage(val isKiswahili: Boolean): BottomBarAction
}