package com.charlesmuchogo.research.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val age: String,
    val educationLevel: String,
    val email: String,
    val firstName: String,
    val gender: String,
    val lastName: String,
    val phone: String,
    val profilePhoto: String,
    val token: String? = null,
    val testedBefore: Boolean,
)
