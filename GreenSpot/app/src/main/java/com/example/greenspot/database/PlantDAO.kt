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
    @Query("SELECT * FROM plant")
    fun getPlants(): Flow<List<Plant>>

    @Query("SELECT * FROM plant WHERE id=(:id)")
    suspend fun getPlant(id: UUID): Plant

    @Update
    suspend fun updatePlant(plant: Plant)

    @Insert
    suspend fun addPlant(plant: Plant)
}
