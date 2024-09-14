package com.charlesmuchogo.research.domain.dto

import com.charlesmuchogo.research.domain.models.TestResult
import kotlinx.serialization.Serializable

@Serializable
data class GetTestResultsDTO(
    val message: String,
    val results: List<TestResult>,
)
