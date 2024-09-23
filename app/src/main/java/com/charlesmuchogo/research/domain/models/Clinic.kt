package com.charlesmuchogo.research.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "Clinics")
data class Clinic(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val address: String,
    val name: String,
    val contacts: String,
    val active: Boolean
)
