﻿package com.example.storeapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class UserLocationModel(
    val id: String,
    val userName: String,
    val street: String,
    val province: String,
    val district: String,
    val ward: String,
    val userId: String,
    val provinceId: String,
    val districtId: String,
    val wardId: String,
//    val latitude: Double? = null,  // Thêm tọa độ GPS
//    val longitude: Double? = null,
//    val createdAt: Timestamp = Timestamp.now(),
//    val updatedAt: Timestamp = Timestamp.now()
) {
    // Secondary constructor with default values
    constructor() : this(
        id = "",
        userName = "",
        street = "",
        province = "",
        district = "",
        ward = "",
        userId = "",
        provinceId = "",
        districtId = "",
        wardId = "",
//        latitude = null,
//        longitude = null,
//        createdAt = Timestamp.now(),
//        updatedAt = Timestamp.now()
    )
}

@Serializable
data class Province(
    val code: Int,
    val name: String,
    @SerialName("division_type") val divisionType: String = "",
    val codename: String = "",        // Thêm trường này
    @SerialName("phone_code") val phoneCode: Int = 0,
    val districts: List<District> = emptyList() // Giữ nguyên
)

@Serializable
data class District(
    val code: Int,
    val name: String,
    @SerialName("division_type") val divisionType: String = "",
    val codename: String = "",        // Thêm trường này
    @SerialName("province_code") val provinceCode: Int = 0,
    val wards: List<Ward> = emptyList() // Giữ nguyên
)

@Serializable
data class Ward(
    val code: Int,
    val name: String,
    @SerialName("division_type") val divisionType: String = "",
    val codename: String = "",        // Thêm trường này
    @SerialName("district_code") val districtCode: Int = 0,
//    val wards: List<Ward> = emptyList() // Giữ nguyên
)
