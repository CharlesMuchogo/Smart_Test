package com.charlesmuchogo.research.domain.dto.results

import com.charlesmuchogo.research.domain.models.TestResult
import kotlinx.serialization.Serializable

@Serializable
data class UploadTestResultsResponse(
    val message: String,
    val result: TestResult
)
