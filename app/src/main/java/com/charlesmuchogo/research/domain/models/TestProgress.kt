package com.charlesmuchogo.research.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TestProgress")
data class TestProgress(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val startTime: Long,
    val timeSpent: Long,
    val active: Boolean = false
)
