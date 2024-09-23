package com.charlesmuchogo.research.domain.dto.login

import com.charlesmuchogo.research.domain.models.Clinic
import kotlinx.serialization.Serializable

@Serializable
data class GetClinicsDTO(
    val message: String,
    val clinics: List<Clinic>
)
