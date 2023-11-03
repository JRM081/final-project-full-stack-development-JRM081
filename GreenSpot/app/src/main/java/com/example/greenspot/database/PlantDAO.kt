package com.example.greenspot.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.greenspot.Plant
import kotlinx.coroutines.flow.Flow
import java.util.UUID


@Dao
interface PlantDao {
    @Query("SELECT * FROM Plant")
    fun getPlants(): Flow<List<Plant>>

    @Query("SELECT * FROM Plant WHERE id=(:id)")
    fun getPlant(id: UUID): LiveData<Plant>

    @Update
    suspend fun updatePlant(plant: Plant): Long

    @Insert
    suspend fun addPlant(plant: Plant): Int
}
