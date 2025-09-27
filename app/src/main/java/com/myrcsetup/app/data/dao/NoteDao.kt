package com.myrcsetup.app.data.dao

import androidx.room.*
import com.myrcsetup.app.data.entity.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    
    @Query("SELECT * FROM notes WHERE id = 1")
    fun getNote(): Flow<Note?>
    
    @Query("SELECT * FROM notes WHERE id = 1")
    suspend fun getNoteSync(): Note?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateNote(note: Note)
    
    @Update
    suspend fun updateNote(note: Note)
    
    @Query("DELETE FROM notes WHERE id = 1")
    suspend fun deleteNote()
}