package com.example.storeapp.model

import com.google.firebase.Timestamp

////User
data class UserModel(
    val id: String = "",
    val role: Int = 0,
    val tier: Int = 0,
    val totalSpent: Long = 0L,
    val defaultLocationId: String = "", // ID địa chỉ mặc định
//    val fullAddress: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val gender: String = "",
    val email: String = "",
    val phone: String = "",
    val vouchers: List<String> = emptyList(),
    val wishList: List<String> = emptyList(),

    val createdAt: Timestamp,
    val updatedAt: Timestamp
){
    constructor() : this(
        id = "",
        role = 0,
        tier = 0,
        totalSpent = 0L,
        defaultLocationId = "",
//        fullAddress = "",
        firstName = "",
        lastName = "",
        gender = "",
        email = "",
        phone = "",
        vouchers = emptyList(),
        wishList = emptyList(),
        createdAt = Timestamp.now(),
        updatedAt = Timestamp.now()
    )
}