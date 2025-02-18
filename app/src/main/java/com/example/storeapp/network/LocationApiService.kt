package com.example.storeapp.network

import com.example.storeapp.model.District
import com.example.storeapp.model.Province
import com.example.storeapp.model.Ward
import retrofit2.http.GET
import retrofit2.http.Path

interface LocationApiService {
    @GET("api/")
    suspend fun getProvinces(): List<Province>

    @GET("api/p/{province_code}?depth=2")
    suspend fun getDistricts(@Path("province_code") provinceCode: Int): Province

    @GET("api/d/{district_code}?depth=2")
    suspend fun getWards(@Path("district_code") districtCode: Int): District
}