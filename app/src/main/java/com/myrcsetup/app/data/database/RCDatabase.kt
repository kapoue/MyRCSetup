package com.myrcsetup.app.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
        
        // Migration de la version 1 vers la version 2 (ajout de la table Note)
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                android.util.Log.d("RCDatabase", "=== DÉBUT MIGRATION 1→2 ===")
                try {
                    // Créer la table notes (nom correct selon l'entité)
                    database.execSQL("""
                        CREATE TABLE IF NOT EXISTS `notes` (
                            `id` INTEGER NOT NULL,
                            `content` TEXT NOT NULL,
                            `lastModified` TEXT NOT NULL,
                            PRIMARY KEY(`id`)
                        )
                    """.trimIndent())
                    android.util.Log.d("RCDatabase", "✅ Table 'notes' créée avec succès")
                } catch (e: Exception) {
                    android.util.Log.e("RCDatabase", "❌ ERREUR lors de la migration: ${e.message}", e)
                    throw e
                }
                android.util.Log.d("RCDatabase", "=== FIN MIGRATION 1→2 ===")
            }
        }
        
        fun getDatabase(context: Context): RCDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RCDatabase::class.java,
                    "rc_database"
                )
                .addMigrations(MIGRATION_1_2)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}