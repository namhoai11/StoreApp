//package com.example.storeapp.data.repository
//
//
//import com.example.storeapp.network.LocationApiService
//import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
//import kotlinx.serialization.json.Json
//import retrofit2.Retrofit
//import okhttp3.MediaType.Companion.toMediaType
//
//
//class NetworkLocationRepository {
//}
//
//
//
//object RetrofitInstance {
//    private const val BASE_URL = "https://provinces.open-api.vn/"
//
//    private val json = Json { ignoreUnknownKeys = true }
//
//    val api: LocationApiService by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
//            .build()
//            .create(LocationApiService::class.java)
//    }
//}