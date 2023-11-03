package com.example.greenspot.database

import androidx.room.TypeConverter
import java.util.Date
import java.util.UUID

class PlantTypeConverters {
    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long): Date {
        return Date(millisSinceEpoch)
    }

        @TypeConverter
        fun fromString(value: String?): UUID? {
            return value?.let { UUID.fromString(it) }
        }

        @TypeConverter
        fun uuidToString(uuid: UUID?): String? {
            return uuid?.toString()
        }
}