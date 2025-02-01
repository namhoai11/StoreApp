package com.example.storeapp.model

import com.google.firebase.Timestamp


data class OrderModel(
    val id: String,
    val userId: String,
    val products: List<ProductDataForOrderModel>,
    val totalPrice: Double,
    val status: String,
    val paymentMethod: String,
    val addressId: String,
    val createdAt: Timestamp,   // Sửa từ `String` thành `Timestamp`
    val updatedAt: Timestamp,   // Sửa từ `String` thành `Timestamp`
    val deletedAt: Timestamp? = null
)
data class ProductDataForOrderModel(
    val id: String,
    val product: ProductModel,
    val orderId: String,
    val quantity: Int
)

//data class OrderModel(
//
//    val id: Int = 0,
//
//    val orderCode: String,
//
//    val totalPrice: Double,
//
//    val orderDate: Timestamp,
//
//    val items: List<CartModel>
//)

