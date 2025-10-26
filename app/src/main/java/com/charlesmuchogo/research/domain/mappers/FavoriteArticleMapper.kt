package com.charlesmuchogo.research.domain.mappers

import com.charlesmuchogo.research.domain.models.Article
import com.charlesmuchogo.research.domain.models.FavoriteArticle

fun Article.toFavoriteArticle(): FavoriteArticle {

    return  FavoriteArticle(
        id = id,
        category = category,
        content = content,
        createdAt = createdAt,
        image = image,
        slug = slug,
        title = title,
        updatedAt = updatedAt,
        userId = userId
    )
}