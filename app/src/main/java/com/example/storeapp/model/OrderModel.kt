package com.example.storeapp.model


data class OrderModel(

    val id: Int = 0,

    val totalPrice: Double,

    val orderDate: Long = System.currentTimeMillis(),

    val items: List<CartModel>
)

