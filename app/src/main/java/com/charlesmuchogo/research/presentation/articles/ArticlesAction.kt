package com.charlesmuchogo.research.presentation.articles

sealed interface ArticlesAction {

    data object OnHasShownAd: ArticlesAction

}