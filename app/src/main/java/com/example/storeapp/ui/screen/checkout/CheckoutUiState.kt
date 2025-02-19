package com.example.storeapp.ui.screen.checkout

import com.example.storeapp.model.CouponModel
import com.example.storeapp.model.ShippingModel
import com.example.storeapp.model.UserLocationModel
import com.example.storeapp.ui.screen.cart.ProductsOnCartToShow

data class CheckoutUiState(
    val selectedLocation: UserLocationModel? = null,
    val products: List<ProductsOnCartToShow> = emptyList(),
    val totalPrice: Double = 0.0,
    val oldTotalPrice: Double = 0.0,
    val finalPrice: Double = 0.0,
    val isShowDialog: Boolean = false,
    val isChooseShipping: Boolean = false,
    val isChooseCoupon: Boolean = false,
    val listShipping: List<ShippingModel> = emptyList(),
    val listCoupon: List<CouponModel> = emptyList(),
    val selectedShipping: ShippingModel? = null,
    val selectedCoupon: CouponModel? = null,
    val note: String = "",
    val selectedPaymentMethod: String = "",
    val isButtonEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val successMessage: String = ""
)
