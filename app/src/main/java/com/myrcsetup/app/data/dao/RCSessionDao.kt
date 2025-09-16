package com.myrcsetup.app.data.dao

import androidx.room.*
import com.myrcsetup.app.data.entity.RCSession
import kotlinx.coroutines.flow.Flow

@Dao
interface RCSessionDao {
    
    @Query("SELECT * FROM rc_sessions ORDER BY dateTime DESC")
    fun getAllSessions(): Flow<List<RCSession>>
    
    @Query("SELECT * FROM rc_sessions WHERE id = :id")
    suspend fun getSessionById(id: Long): RCSession?
    
    @Insert
    suspend fun insertSession(session: RCSession): Long
    
    @Update
    suspend fun updateSession(session: RCSession)
    
    @Delete
    suspend fun deleteSession(session: RCSession)
    
    @Query("DELETE FROM rc_sessions WHERE id = :id")
    suspend fun deleteSessionById(id: Long)
    
    @Query("SELECT * FROM rc_sessions WHERE carName LIKE '%' || :searchQuery || '%' OR trackName LIKE '%' || :searchQuery || '%' ORDER BY dateTime DESC")
    fun searchSessions(searchQuery: String): Flow<List<RCSession>>
    
    @Query("SELECT COUNT(*) FROM rc_sessions")
    suspend fun getSessionCount(): Int
}