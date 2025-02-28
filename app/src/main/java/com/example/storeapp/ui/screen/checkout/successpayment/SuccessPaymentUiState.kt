package com.example.storeapp.ui.screen.checkout.successpayment

import com.example.storeapp.model.OrderModel

data class SuccessPaymentUiState(
    val currentOrder: OrderModel = OrderModel(),
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val successMessage: String = ""
)