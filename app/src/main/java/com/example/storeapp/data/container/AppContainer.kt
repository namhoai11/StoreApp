package com.example.storeapp.data.container

import com.example.storeapp.data.repository.FirebaseAuthRepository
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import com.example.storeapp.data.repository.RealtimeDatabaseRepository

interface AppContainer {
    val realtimeDatabaseRepository: RealtimeDatabaseRepository
    val firebaseAuthRepository: FirebaseAuthRepository
    val firebaseFireStoreRepository: FirebaseFireStoreRepository
}

class AppDataContainer : AppContainer {
    override val realtimeDatabaseRepository: RealtimeDatabaseRepository by lazy {
        RealtimeDatabaseRepository()
    }
    override val firebaseAuthRepository: FirebaseAuthRepository by lazy {
        FirebaseAuthRepository()
    }
    override val firebaseFireStoreRepository:FirebaseFireStoreRepository by lazy {
        FirebaseFireStoreRepository()
    }
}