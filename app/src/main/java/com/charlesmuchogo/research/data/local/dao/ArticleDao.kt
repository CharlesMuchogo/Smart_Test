package com.charlesmuchogo.research.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.charlesmuchogo.research.domain.models.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article)

    @Upsert
    suspend fun insertArticles(articles: List<Article>)

    @Query("SELECT * FROM Article ORDER BY createdAt DESC")
    fun pagingSource(): PagingSource<Int, Article>

    @Query("SELECT COUNT(*) FROM Article")
    suspend fun count(): Int

    @Query("SELECT * FROM Article WHERE id = :id ORDER BY id ASC LIMIT 1")
    fun getArticle(id: String): Flow<Article?>


    @Query("SELECT * FROM Article ORDER BY createdAt DESC")
    fun getArticles(): Flow<List<Article>>

    @Update
    suspend fun updateArticle(article: Article)

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("DELETE FROM Article")
    suspend fun deleteArticles()
}
