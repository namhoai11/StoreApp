package com.example.storeapp.ui.screen.checkout

import androidx.lifecycle.ViewModel
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import com.example.storeapp.ui.screen.cart.ProductsOnCartToShow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CheckoutViewModel(
    private val repository: FirebaseFireStoreRepository
) : ViewModel() {
    private val _checkoutUiState = MutableStateFlow(CheckoutUiState())
    val checkoutUiState: StateFlow<CheckoutUiState> = _checkoutUiState

    fun proceedToCheckout(cartProducts: List<ProductsOnCartToShow>) {
        if (cartProducts.isEmpty()) {
            _checkoutUiState.update { it.copy(errorMessage = "Giỏ hàng trống!") }
            return
        }

        val totalPrice = cartProducts.sumOf { it.productTotalPrice }

        _checkoutUiState.update {
            it.copy(
                products = cartProducts,
                totalPrice = totalPrice,
                oldTotalPrice = totalPrice,  // Lưu giá trước khi áp dụng giảm giá
                shippingCost = 0.0,          // Ban đầu chưa có phí ship
                shippingDescription = "",
                voucherId = "",
                note = "",
                selectedPaymentMethod = "",
                selectedAddressId = ""
            )
        }
    }
}
