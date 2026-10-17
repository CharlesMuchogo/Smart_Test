package com.charlesmuchogo.research.presentation.results

import com.charlesmuchogo.research.domain.models.TestResult
import java.io.File

data class ResultsState(
    val result: TestResult? = null,
    val showDeleteDialog: Boolean = false,
    val showMore: Boolean = false,

    val isDeleting: Boolean = false,
    val hasDeleted: Boolean = false,
    val isExportingPdf: Boolean = false,
)

sealed interface ResultsEvent {
    data class OpenPdf(val file: File, val share: Boolean) : ResultsEvent
}