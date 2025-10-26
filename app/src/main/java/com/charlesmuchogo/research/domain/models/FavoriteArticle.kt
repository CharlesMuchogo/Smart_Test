package com.charlesmuchogo.research.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "FavoriteArticle")
data class FavoriteArticle(
    @SerialName("_id")
    @PrimaryKey
    val  id: String,
    val category: String,
    val content: String,
    val createdAt: String,
    val image: String,
    val slug: String,
    val title: String,
    val updatedAt: String,
    val userId: String
)
