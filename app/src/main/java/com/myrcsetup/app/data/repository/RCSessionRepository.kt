package com.myrcsetup.app.data.repository

import com.myrcsetup.app.data.dao.RCSessionDao
import com.myrcsetup.app.data.entity.RCSession
import kotlinx.coroutines.flow.Flow

class RCSessionRepository(private val sessionDao: RCSessionDao) {
    
    fun getAllSessions(): Flow<List<RCSession>> = sessionDao.getAllSessions()
    
    suspend fun getSessionById(id: Long): RCSession? = sessionDao.getSessionById(id)
    
    suspend fun insertSession(session: RCSession): Long = sessionDao.insertSession(session)
    
    suspend fun updateSession(session: RCSession) = sessionDao.updateSession(session)
    
    suspend fun deleteSession(session: RCSession) = sessionDao.deleteSession(session)
    
    suspend fun deleteSessionById(id: Long) = sessionDao.deleteSessionById(id)
    
    fun searchSessions(searchQuery: String): Flow<List<RCSession>> = 
        sessionDao.searchSessions(searchQuery)
    
    suspend fun getSessionCount(): Int = sessionDao.getSessionCount()
}