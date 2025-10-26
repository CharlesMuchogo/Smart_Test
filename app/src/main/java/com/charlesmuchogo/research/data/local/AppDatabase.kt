package com.charlesmuchogo.research.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.charlesmuchogo.research.data.local.dao.ArticleDao
import com.charlesmuchogo.research.data.local.dao.ClinicsDao
import com.charlesmuchogo.research.data.local.dao.FavoriteArticleDao
import com.charlesmuchogo.research.data.local.dao.MessagesDao
import com.charlesmuchogo.research.data.local.dao.TestProgressDao
import com.charlesmuchogo.research.data.local.dao.TestResultsDao
import com.charlesmuchogo.research.data.local.dao.UserDao
import com.charlesmuchogo.research.domain.models.Article
import com.charlesmuchogo.research.domain.models.Clinic
import com.charlesmuchogo.research.domain.models.FavoriteArticle
import com.charlesmuchogo.research.domain.models.Message
import com.charlesmuchogo.research.domain.models.TestProgress
import com.charlesmuchogo.research.domain.models.TestResult
import com.charlesmuchogo.research.domain.models.User

@Database(entities = [User::class, TestResult::class, Clinic::class, TestProgress::class, Message::class, Article::class, FavoriteArticle::class], version = 8, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun clinicsDao(): ClinicsDao
    abstract fun testResultsDao(): TestResultsDao
    abstract fun testProgressDao(): TestProgressDao
    abstract fun messagesDao(): MessagesDao

    abstract fun articleDao(): ArticleDao

    abstract fun favoriteArticleDao(): FavoriteArticleDao
}
