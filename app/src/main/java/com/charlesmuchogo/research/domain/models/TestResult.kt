package com.charlesmuchogo.research.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "TestResults")
data class TestResult(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val care_option: String,
    val uuid: String,
    val date: String,
    val image: String,
    val status: String = "pending",
    val partnerImage: String,
    val partnerResults: String,
    val results: String,
    val userId: Long,
)
