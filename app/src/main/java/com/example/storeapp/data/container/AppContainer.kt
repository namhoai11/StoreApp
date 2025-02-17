package com.example.storeapp.data.container

import com.example.storeapp.data.repository.FirebaseAuthRepository
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import com.example.storeapp.data.repository.RealtimeDatabaseRepository
import com.example.storeapp.network.LocationApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val realtimeDatabaseRepository: RealtimeDatabaseRepository
    val firebaseAuthRepository: FirebaseAuthRepository
    val firebaseFireStoreRepository: FirebaseFireStoreRepository
    val locationApiService: LocationApiService  // Thêm API địa điểm
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

    private val baseUrl = "https://provinces.open-api.vn/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    override val locationApiService: LocationApiService by lazy {
        retrofit.create(LocationApiService::class.java)
    }
}