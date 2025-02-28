package com.example.storeapp.model

data class ShippingModel(
    val name: String,
    val price: Double,
    val description: String,
    val estimatedDeliveryDays: Int // Số ngày dự kiến giao hàng
)

