package com.example.storeapp.data.container

import com.example.storeapp.data.repository.FirebaseStoreAppRepository
import com.example.storeapp.data.repository.StoreAppRepository

interface AppContainer {
    val storeAppRepository: StoreAppRepository
}

class AppDataContainer : AppContainer {
    override val storeAppRepository: StoreAppRepository by lazy {
        FirebaseStoreAppRepository()
    }
}