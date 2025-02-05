package com.example.storeapp.data.container

import com.example.storeapp.data.repository.FirebaseAuthRepository
import com.example.storeapp.data.repository.RealtimeDatabaseRepository

interface AppContainer {
    val realtimeDatabaseRepository: RealtimeDatabaseRepository
    val firebaseAuthRepository:FirebaseAuthRepository
}

class AppDataContainer : AppContainer {
    override val realtimeDatabaseRepository: RealtimeDatabaseRepository by lazy {
        RealtimeDatabaseRepository()
    }
    override val firebaseAuthRepository: FirebaseAuthRepository by lazy {
        FirebaseAuthRepository()
    }
}