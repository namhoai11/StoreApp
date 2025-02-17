package com.example.storeapp.ui.screen.checkout

import com.example.storeapp.ui.screen.cart.ProductsOnCartToShow

data class CheckoutUiState(
    val products: List<ProductsOnCartToShow> = emptyList(),
    val totalPrice: Double = 0.0,
    val oldTotalPrice: Double = 0.0,
    val voucherId: String = "",
    val shippingCost: Double = 0.0,
    val shippingDescription: String = "",
    val note: String = "",
    val selectedPaymentMethod: String = "",
    val selectedAddressId: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val successMessage: String = ""
)
