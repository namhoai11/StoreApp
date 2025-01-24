package com.example.storeapp.model


data class Order(

    val id: Int = 0,

    val totalPrice: Double,

    val orderDate: Long = System.currentTimeMillis(),

    val items: List<Cart>
)

