package com.charlesmuchogo.research.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.charlesmuchogo.research.data.local.dao.TestResultsDao
import com.charlesmuchogo.research.data.local.dao.UserDao
import com.charlesmuchogo.research.domain.models.TestResult
import com.charlesmuchogo.research.domain.models.User

@Database(entities = [User::class, TestResult::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun testResultsDao(): TestResultsDao
}
