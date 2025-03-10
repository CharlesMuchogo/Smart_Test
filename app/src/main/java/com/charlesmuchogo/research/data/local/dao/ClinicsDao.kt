package com.charlesmuchogo.research.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.charlesmuchogo.research.domain.models.Clinic
import kotlinx.coroutines.flow.Flow

@Dao
interface ClinicsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClinics(clinics: List<Clinic>)

    @Query("SELECT * FROM Clinics ORDER BY name ASC")
    fun getClinics(): Flow<List<Clinic>>


    @Query("SELECT * FROM Clinics WHERE name LIKE '%' || :searchTerm || '%' OR  address LIKE  '%' || :searchTerm || '%' OR  contacts LIKE  '%' || :searchTerm || '%'  ORDER BY name ASC")
    fun searchClinics(searchTerm: String): Flow<List<Clinic>>

    @Query("DELETE FROM Clinics")
    suspend fun deleteClinics()
}
