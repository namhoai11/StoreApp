package com.example.storeapp

import android.app.Application
import com.example.storeapp.data.container.AppContainer
import com.example.storeapp.data.container.AppDataContainer

class StoreAppManagerApplication:Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer()
    }
}