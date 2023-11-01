package com.example.greenspot.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.greenspot.Plant
import java.util.Date


@Database(entities = [ Plant::class ], version=1)
@TypeConverters(PlantTypeConverters::class)
abstract class PlantDatabase : RoomDatabase() {
    abstract fun plantDao(): PlantDao

}
class PlantTypeConverters {
    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }
    @TypeConverter
    fun toDate(millisSinceEpoch: Long): Date {
        return Date(millisSinceEpoch)
    }
}
