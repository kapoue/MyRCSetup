package com.myrcsetup.app.data.repository

import com.myrcsetup.app.data.dao.NoteDao
import com.myrcsetup.app.data.entity.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class NoteRepository(private val noteDao: NoteDao) {
    
    fun getNote(): Flow<Note?> = noteDao.getNote()
    
    suspend fun getNoteSync(): Note? = noteDao.getNoteSync()
    
    suspend fun saveNote(content: String) {
        val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val note = Note(
            id = 1,
            content = content,
            lastModified = currentTime
        )
        noteDao.insertOrUpdateNote(note)
    }
    
    suspend fun deleteNote() {
        noteDao.deleteNote()
    }
}