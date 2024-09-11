package com.charlesmuchogo.research.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.charlesmuchogo.research.domain.models.TestResult
import com.charlesmuchogo.research.domain.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface TestResultsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestResult(result: TestResult)

    @Query("SELECT * FROM TESTRESULTS ORDER BY id DESC")
    fun getTestResults(): Flow<List<TestResult>>

    @Update
    suspend fun updateResult(result: TestResult)

    @Delete
    suspend fun deleteResult(result: TestResult)
}