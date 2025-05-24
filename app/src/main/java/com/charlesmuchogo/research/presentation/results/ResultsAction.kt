package com.charlesmuchogo.research.presentation.results

sealed interface ResultsAction {
    data class OnLoadResult(val id: Long): ResultsAction
    data class OnShowMoreChange(val show: Boolean): ResultsAction
    data class OnShowDeleteDialog(val show: Boolean): ResultsAction
}