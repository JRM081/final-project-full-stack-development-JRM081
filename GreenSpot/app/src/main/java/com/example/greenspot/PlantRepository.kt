package com.example.greenspot

import android.content.Context
import androidx.room.Room
import com.example.greenspot.database.PlantDatabase
import java.util.UUID

private const val DATABASE_NAME = "plant-database"

class PlantRepository  private constructor(context: Context) {

    private val database: PlantDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            PlantDatabase::class.java,
            DATABASE_NAME
        )
        .build()

    suspend fun getCrimes(): List<Plant> = database.plantDao().getPlants()
    suspend fun getCrime(id: UUID): Plant = database.plantDao().getPlant(id)

    companion object {
        private var INSTANCE: PlantRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = PlantRepository(context)
            }
        }
        fun get(): PlantRepository {
            return INSTANCE ?:
            throw IllegalStateException("PlantRepository must be initialized")
        }
    }
}
