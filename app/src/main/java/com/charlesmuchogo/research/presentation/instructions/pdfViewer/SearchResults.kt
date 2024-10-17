package com.charlesmuchogo.research.presentation.instructions.pdfViewer

import android.graphics.RectF

data class SearchResults(
    val page: Int,
    val results: List<RectF>
)
