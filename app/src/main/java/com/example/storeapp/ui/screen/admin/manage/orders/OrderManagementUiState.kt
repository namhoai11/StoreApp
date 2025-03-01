package com.example.storeapp.ui.screen.admin.manage.orders

import com.example.storeapp.model.OrderModel
import com.example.storeapp.model.OrderStatus
import com.example.storeapp.model.UserModel

data class OrderManagementUiState(
    val allOrder: List<OrderModel> = emptyList(),
    val currentOrderStatus: OrderStatus=OrderStatus.ALL,
    val ordersByStatus: Map<OrderStatus, List<OrderModel>> = emptyMap(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,

    val ordersSearched: List<OrderModel> = emptyList(),
    val currentQuery: String = "",

    val userMap: Map<String, UserModel> = emptyMap()
) {
    val currentListOrders: List<OrderModel>
        get() = when (currentOrderStatus) {
            OrderStatus.ALL -> allOrder // Hiển thị tất cả mục
            else -> ordersByStatus[currentOrderStatus] ?: emptyList()
        }
}