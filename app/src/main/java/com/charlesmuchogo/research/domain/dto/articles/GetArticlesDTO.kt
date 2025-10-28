package com.charlesmuchogo.research.domain.dto.articles

import com.charlesmuchogo.research.domain.models.Article
import kotlinx.serialization.Serializable

@Serializable
data class GetArticlesDTO(
    val lastMonthPosts: Long,
    val posts: List<Article>,
    val totalPosts: Long,
    val lastPage: Int
)