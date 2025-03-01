package com.example.storeapp.ui.screen.admin.manage.orders.orderdetailsmanagement

import com.example.storeapp.model.OrderModel
import com.example.storeapp.model.UserModel

data class OrderDetailsManagementUiState(
    val order: OrderModel = OrderModel(),
    val user: UserModel? = null,
//    val isShowPaymentDialog: Boolean = false,
//    val isShowCanceledOrderDialog:Boolean=false,

    val isShowCanceledOrderDialog:Boolean=false,
    val isShowCompletedOrderDialog:Boolean=false,

    val currentButtonText: String = "",

    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)