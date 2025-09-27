package com.myrcsetup.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey
    val id: Long = 1, // ID fixe car il n'y a qu'une seule note
    val content: String,
    val lastModified: LocalDateTime
)