package com.charlesmuchogo.research.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.charlesmuchogo.research.domain.models.Clinic
import com.charlesmuchogo.research.domain.models.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessagesDao {

    @Upsert()
    suspend fun insertMessages(messages: List<Message>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message)

    @Query("SELECT * FROM Messages ORDER BY timestamp DESC")
    fun getMessages(): Flow<List<Message>>


    @Query("SELECT * FROM Messages WHERE message LIKE '%' || :searchTerm || '%'  ORDER BY timestamp DESC")
    fun searchMessages(searchTerm: String): Flow<List<Message>>

    @Query("DELETE FROM Messages")
    suspend fun deleteMessages()
}
