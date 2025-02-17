package com.example.storeapp.model

import com.google.firebase.Timestamp
import kotlinx.serialization.Serializable


data class UserLocationModel(
    val id: String,
    val userName:String,
    val street: String,
    val province: String,
    val district: String,
    val ward: String,
//    val isDefault: Boolean,
    val userId: String,
    val provinceId: String,
    val districtId: String,
    val wardId: String,
    val latitude: Double? = null,  // Thêm tọa độ GPS
    val longitude: Double? = null,
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now()
)

@Serializable
data class Province(
    val code: Int,
    val name: String
)

@Serializable
data class District(
    val code: Int,
    val name: String
)

@Serializable
data class Ward(
    val code: Int,
    val name: String
)
