package com.example.greenspot


import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class PlantListViewModel : ViewModel() {
    val plants = mutableListOf<Plant>()
    init {
        Log.d(TAG, "init starting")
        viewModelScope.launch {
            Log.d(TAG, "coroutine launched")
            plants += loadPlants()
            Log.d(TAG, "Loading crimes finished")
        }
    }

    suspend fun loadPlants(): List<Plant> {
        val result = mutableListOf<Plant>()
        delay(5000)
        for (i in 0 until 100) {
            val plant = Plant(
                id = UUID.randomUUID(),
                title = "Plant #$i",
                date = Date(),
                place = "Location #$i"
            )
            result += plant
        }
        return result
    }

}
