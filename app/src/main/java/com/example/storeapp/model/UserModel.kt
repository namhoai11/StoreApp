package com.example.storeapp.model

import com.google.firebase.Timestamp

data class UserModel(
    val id: String = "",
    val role: Int = 0,
    val tier: Int = 0,
    val totalSpent: Long = 0L,
    val addressId: String = "",
    val fullAddress: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val gender: String = "",
    val email: String = "",
    val phone: String = "",
    val vouchers: List<String> = emptyList(),
    val wishlist: List<String> = emptyList(),

    val createdAt: Timestamp,
    val updatedAt: Timestamp
)