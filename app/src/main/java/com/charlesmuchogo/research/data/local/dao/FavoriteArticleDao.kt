package com.charlesmuchogo.research.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.charlesmuchogo.research.domain.models.FavoriteArticle
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: FavoriteArticle)

    @Upsert
    suspend fun insertArticles(articles: List<FavoriteArticle>)

    @Query("SELECT * FROM FavoriteArticle WHERE id = :id ORDER BY id ASC LIMIT 1")
    fun getArticle(id: String): Flow<FavoriteArticle?>


    @Query("SELECT * FROM FavoriteArticle ORDER BY createdAt DESC")
    fun getArticles(): Flow<List<FavoriteArticle>>

    @Update
    suspend fun updateArticle(article: FavoriteArticle)

    @Delete
    suspend fun deleteArticle(article: FavoriteArticle)

    @Query("DELETE FROM FavoriteArticle")
    suspend fun deleteArticles()
}
