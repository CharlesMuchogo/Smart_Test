package com.charlesmuchogo.research.presentation.results

import com.charlesmuchogo.research.domain.models.TestResult

data class ResultsState(
    val result: TestResult? = null,
    val showDeleteDialog: Boolean = false,
    val showMore: Boolean = false,

    val isDeleting: Boolean = false,
    val hasDeleted: Boolean = false
)