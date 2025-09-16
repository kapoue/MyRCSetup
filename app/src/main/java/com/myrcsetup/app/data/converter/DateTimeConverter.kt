package com.myrcsetup.app.data.converter

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDateTime

class DateTimeConverter {
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? {
        return dateTime?.toString()
    }

    @TypeConverter
    fun toLocalDateTime(dateTimeString: String?): LocalDateTime? {
        return dateTimeString?.let { LocalDateTime.parse(it) }
    }
}