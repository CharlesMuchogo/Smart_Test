package com.charlesmuchogo.research.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.charlesmuchogo.research.data.local.dao.UserDao
import com.charlesmuchogo.research.domain.models.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun userDao(): UserDao
}