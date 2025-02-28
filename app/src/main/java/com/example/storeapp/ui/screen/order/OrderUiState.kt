package com.example.storeapp.ui.screen.order

import com.example.storeapp.model.OrderModel
import com.example.storeapp.model.OrderStatus

data class OrderUiState(
    val listOrder: List<OrderModel> = emptyList(),
    val currentOrderStatus: OrderStatus = OrderStatus.ALL,
    val listOrderByStatus: Map<OrderStatus, List<OrderModel>> = emptyMap(),

    val ordersSearched: List<OrderModel> = emptyList(),
    val currentQuery: String = "",

    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
) {
    val currentListOrder: List<OrderModel>
        get() = when (currentOrderStatus) {
            OrderStatus.ALL -> listOrder
            else -> listOrderByStatus[currentOrderStatus] ?: emptyList()
        }
}