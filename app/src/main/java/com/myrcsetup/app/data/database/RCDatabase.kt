package com.myrcsetup.app.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.myrcsetup.app.data.converter.DateTimeConverter
import com.myrcsetup.app.data.dao.RCSessionDao
import com.myrcsetup.app.data.dao.NoteDao
import com.myrcsetup.app.data.entity.RCSession
import com.myrcsetup.app.data.entity.Note

@Database(
    entities = [RCSession::class, Note::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(DateTimeConverter::class)
abstract class RCDatabase : RoomDatabase() {
    
    abstract fun sessionDao(): RCSessionDao
    abstract fun noteDao(): NoteDao
    
    companion object {
        @Volatile
        private var INSTANCE: RCDatabase? = null
        
        fun getDatabase(context: Context): RCDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RCDatabase::class.java,
                    "rc_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}