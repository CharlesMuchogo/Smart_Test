package com.charlesmuchogo.research.presentation.bottomBar

import com.charlesmuchogo.research.presentation.instructions.InstructionsAction

sealed interface BottomBarAction {

    data class OnUpdateSelectedItem(val item: Int): BottomBarAction

    data class OnUpdateSelectedLanguage(val isKiswahili: Boolean): BottomBarAction

    data object OnHasShownAd: BottomBarAction
}