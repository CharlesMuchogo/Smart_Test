package com.charlesmuchogo.research.domain.dto.results

import kotlinx.serialization.Serializable

@Serializable
data class UploadTestResultsDTO(
    val image: ByteArray,
    val partnerImage: ByteArray? = null,
    val careOption: Long? = null,
)
