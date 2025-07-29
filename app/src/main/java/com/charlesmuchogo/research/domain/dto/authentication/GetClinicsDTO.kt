package com.charlesmuchogo.research.domain.dto.authentication

import com.charlesmuchogo.research.domain.models.Clinic
import kotlinx.serialization.Serializable

@Serializable
data class GetClinicsDTO(
    val message: String,
    val clinics: List<Clinic>
)
