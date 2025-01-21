package com.example.storeapp.model

data class UserLocation(
    val id: Int = 0,
    val name: String,
    val address: String,
    val latitude: Double? = null, // Tọa độ GPS, có thể null nếu không cần
    val longitude: Double? = null,
    val isPrimary: Boolean = false, // Địa điểm chính
    val createdAt: Long = System.currentTimeMillis() // Thời gian tạo
) {
    init {
        require(name.isNotBlank()) { "Location name cannot be blank." }
        require(address.isNotBlank()) { "Address cannot be blank." }
    }
}
