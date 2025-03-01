package com.example.storeapp.ui.screen.admin.dashboard

import com.example.storeapp.model.OrderModel

data class DashBoardUiState(
    val allOrder: List<OrderModel> = emptyList(),
    val listOrderOfMonth: List<OrderModel> = emptyList(),
    val listOrderProcessing: List<OrderModel> = emptyList(),
    val listNewOrder: List<OrderModel> = emptyList(),
    val revenueThisMonth: Double = 0.0,
    val revenueThisDay: Double = 0.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
)