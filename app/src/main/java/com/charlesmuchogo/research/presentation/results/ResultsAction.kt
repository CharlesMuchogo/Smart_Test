package com.charlesmuchogo.research.presentation.results

sealed interface ResultsAction {
    data class OnLoadResult(val id: Long): ResultsAction
}