package com.charlesmuchogo.research.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "Messages")
data class Message(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val message: String,
    val sender: String,
    val timestamp: Long
)
