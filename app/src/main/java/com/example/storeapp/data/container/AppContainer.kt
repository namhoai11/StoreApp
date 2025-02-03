package com.example.storeapp.data.container

import com.example.storeapp.data.repository.RealtimeDatabaseRepository

interface AppContainer {
    val realtimeDatabaseRepository: RealtimeDatabaseRepository
}

class AppDataContainer : AppContainer {
    override val realtimeDatabaseRepository: RealtimeDatabaseRepository by lazy {
        RealtimeDatabaseRepository()
    }
}