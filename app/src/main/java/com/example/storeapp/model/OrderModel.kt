package com.example.storeapp.model

import com.example.storeapp.ui.screen.cart.ProductsOnCartToShow
import com.google.firebase.Timestamp


data class OrderModel(
    val orderCode: String = "",
    val userId: String = "",
    val products: List<ProductsOnCartToShow> = emptyList(),
    val totalPrice: Double = 0.0,
    val note: String = "",
    val status: OrderStatus = OrderStatus.PENDING,
    val paymentMethod: String = "",
    val address: UserLocationModel = UserLocationModel(),
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now(),
    val deletedAt: Timestamp? = null,
    val estimatedDeliveryDate: Timestamp = Timestamp.now(), // Ngày nhận dự kiến
) {
    constructor() : this(
        "",
        "",
        emptyList(),
        0.0,
        "",
        OrderStatus.PENDING,
        "",
        UserLocationModel(),
        Timestamp.now(),
        Timestamp.now(),
        null,
        Timestamp.now(),

        )
}


enum class OrderStatus() {
    ALL,
    AWAITING_PAYMENT,
    PENDING,
    CONFIRMED,
    SHIPPED,
    COMPLETED,
    CANCELED;

    override fun toString(): String {
        return when (this) {
            ALL -> "Tất cả"
            AWAITING_PAYMENT -> "Chờ thanh toán"
            PENDING -> "Chờ Xác nhận"
            CONFIRMED -> "Đã Xác nhận"
            SHIPPED -> "Đang giao"
            COMPLETED -> "Hoàn Thành"
            CANCELED -> "Đã Hủy"
        }
    }

    companion object {
        fun fromString(value: String): OrderStatus? {
            return OrderStatus.entries.find { it.toString() == value }
        }
    }
}


