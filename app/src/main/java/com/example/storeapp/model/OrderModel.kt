package com.example.storeapp.model

import com.example.storeapp.ui.screen.cart.ProductsOnCartToShow
import com.google.firebase.Timestamp


data class OrderModel(
    val orderCode: String,
    val userId: String,
    val products: List<ProductsOnCartToShow>,
    val totalPrice: Double,
    val note: String = "",
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

enum class OrderStatus() {
    ALL,
    PENDING,
    CONFIRMED,
    SHIPPED,
    COMPLETED,
    CANCELED;

    override fun toString(): String {
        return when (this) {
            ALL -> "Tất cả"
            PENDING -> "Chờ Xác nhận"
            CONFIRMED -> "Đã Xác nhận"
            SHIPPED -> "Đang giao"
            COMPLETED -> "Hoàn Thành"
            CANCELED -> "Đã Hủy"
        }
    }
}

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

