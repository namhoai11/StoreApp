package com.example.storeapp.ui.screen.checkout.payment

import com.example.storeapp.model.OrderModel
import com.example.storeapp.model.PaymentMethodModel

data class PaymentUiState(
    val listPaymentMethodModel: List<PaymentMethodModel> = emptyList(),
    val paymentMethodSelected: PaymentMethodModel? = null,
    val currentOrder: OrderModel = OrderModel(),
    val isButtonEnabled: Boolean = true,
    val isShowDialog: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val successMessage: String = ""
)