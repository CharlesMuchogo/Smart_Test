package com.charlesmuchogo.research.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "Article")
data class Article(
    @PrimaryKey
    val  id: String,
    @SerialName("categoryId")
    val category: String,
    val content: String,
    val createdAt: String,
    val image: String,
    val slug: String,
    val title: String,
    val updatedAt: String,
    val userId: String,
    val isFavorite: Boolean = false
)
