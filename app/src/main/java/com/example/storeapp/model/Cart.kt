package com.example.storeapp.model


data class Cart(
    var id: Int = 0,
    var productId: Int = 0,
    var productName: String = "",
    var productPrice: String = "",
    var productImage: String = "",
    var productCategory: String = "",
    var productQuantity: Int = 0
)

