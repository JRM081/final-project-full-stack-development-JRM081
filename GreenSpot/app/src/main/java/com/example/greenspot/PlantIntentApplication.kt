package com.example.greenspot

import android.app.Application

class PlantApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        PlantRepository.initialize(this)
    }
}
