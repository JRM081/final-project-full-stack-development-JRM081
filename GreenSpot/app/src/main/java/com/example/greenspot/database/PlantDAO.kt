package com.example.greenspot.database

import androidx.room.Dao
import androidx.room.Query
import com.example.greenspot.Plant
import java.util.UUID

@Dao
interface PlantDao {
    @Query("SELECT * FROM plant")
    suspend fun getPlants(): List<Plant>
    @Query("SELECT * FROM plant WHERE id=(:id)")
    suspend fun getPlant(id: UUID): Plant
}
