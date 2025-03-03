package com.example.storeapp.model

import com.google.firebase.Timestamp

data class UserModel(
    val id: String = "",
    val role: Role = Role.USER,
    val tier: Int = 0,
    val totalSpent: Long = 0L,
    val defaultLocationId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val gender: Gender = Gender.OTHER,
    val dateOfBirth: Timestamp = Timestamp.now(),
    val email: String = "",
    val phone: String = "",
    val vouchers: List<String> = emptyList(),
    val wishList: List<String> = emptyList(),
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now()
) {
    constructor() : this(
        id = "",
        role = Role.USER,
        tier = 0,
        totalSpent = 0L,
        defaultLocationId = "",
        firstName = "",
        lastName = "",
        gender = Gender.OTHER, // Giá trị mặc định
        dateOfBirth = Timestamp.now(), // Giá trị mặc định
        email = "",
        phone = "",
        vouchers = emptyList(),
        wishList = emptyList(),
        createdAt = Timestamp.now(),
        updatedAt = Timestamp.now()
    )
}

enum class Gender {
    MALE,
    FEMALE,
    OTHER;

    override fun toString(): String {
        return when (this) {
            MALE -> "Nam"
            FEMALE -> "Nữ"
            OTHER -> "Khác"
        }
    }

    companion object {
        fun fromString(value: String): Gender? {
            return entries.find { it.toString() == value }
        }
    }
}

enum class Role {
    USER,
    ADMIN;

    override fun toString(): String {
        return when (this) {
            USER -> "Người dùng"
            ADMIN -> "Quản trị"
        }
    }

    companion object {
        fun fromString(value: String): Role? {
            return entries.find { it.toString() == value }
        }
    }
}

