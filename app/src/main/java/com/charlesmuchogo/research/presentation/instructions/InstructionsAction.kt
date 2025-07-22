package com.charlesmuchogo.research.presentation.instructions

sealed interface InstructionsAction {

    data object OnHasShownAd: InstructionsAction

}