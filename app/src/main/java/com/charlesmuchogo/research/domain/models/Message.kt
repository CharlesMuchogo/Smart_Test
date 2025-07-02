package com.charlesmuchogo.research.domain.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable

@Entity(tableName = "Messages", indices = [Index(value = ["timestamp"], unique = true)])
data class Message(
    @Transient
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val message: String,
    val sender: String,
    val timestamp: Long
)
