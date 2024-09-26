package com.charlesmuchogo.research.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.charlesmuchogo.research.domain.models.TestProgress
import com.charlesmuchogo.research.domain.models.TestResult
import kotlinx.coroutines.flow.Flow

@Dao
interface TestProgressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestProgress(progress: TestProgress)

    @Query("SELECT * FROM TestProgress WHERE active = 1 LIMIT 1")
    fun getActiveTestProgress(): Flow<TestProgress?>

    @Update
    suspend fun updateTestProgress(progress: TestProgress)

    @Delete
    suspend fun deleteTestProgress(progress: TestProgress)

    @Query("DELETE FROM TestProgress")
    suspend fun clearTestProgress()
}
