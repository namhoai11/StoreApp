package com.example.storeapp.ui.screen.order.orderdetails

import com.example.storeapp.model.OrderModel

data class OrderDetailsUiState(
    val order: OrderModel = OrderModel(),
    val isShowPaymentDialog: Boolean = false,
    val isShowCanceledOrderDialog:Boolean=false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)