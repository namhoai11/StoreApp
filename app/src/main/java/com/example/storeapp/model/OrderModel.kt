package com.example.storeapp.model

import com.google.firebase.Timestamp


data class OrderModel(

    val id: Int = 0,

    val orderCode: String,

    val totalPrice: Double,

    val orderDate: Timestamp,

    val items: List<CartModel>
)

