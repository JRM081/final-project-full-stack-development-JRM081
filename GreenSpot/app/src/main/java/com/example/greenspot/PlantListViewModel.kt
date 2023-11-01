package com.example.greenspot

import androidx.lifecycle.ViewModel
import java.util.Date
import java.util.UUID

class PlantListViewModel : ViewModel() {
    val plants = mutableListOf<Plant>()
    init {
        for (i in 0 until 100) {
            val plant = Plant(
                id = UUID.randomUUID(),
                title ="Plant #$i",
                date = Date(),
                place ="Location #$i",
            )
            plants += plant
        }
    }
}
