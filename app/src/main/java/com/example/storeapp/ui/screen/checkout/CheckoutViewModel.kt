﻿package com.example.storeapp.ui.screen.checkout

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.local.DataDummy
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import com.example.storeapp.model.CouponModel
import com.example.storeapp.model.CouponType
import com.example.storeapp.model.ShippingModel
import com.example.storeapp.model.UserModel
import com.example.storeapp.ui.screen.cart.ProductsOnCartToShow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CheckoutViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: FirebaseFireStoreRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState

    private val _user = MutableLiveData<UserModel?>()
    val user: LiveData<UserModel?> = _user


    private val locationId: String? = savedStateHandle["locationId"]


    init {
        loadUser()
        viewModelScope.launch {
            _uiState.collect { state ->
                Log.d("CheckoutViewModel", "Current UI State: $state")
            }
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            val userData = repository.getCurrentUser()
            _user.value = userData
            Log.d("CheckoutViewModel", "User loaded: $userData")
            loadCheckout()

        }
    }

    private fun loadCheckout() = viewModelScope.launch {
        try {
            _uiState.update { it.copy(isLoading = true) }
            val currentUser = _user.value
            if (currentUser == null) {
                _uiState.update { it.copy(errorMessage = "Không thể xác định người dùng") }
                return@launch
            }
//------------d/c
            val locationResult = repository.getListAddressByUser(currentUser.id)
            if (locationResult.isSuccess) {
                val listAddress = locationResult.getOrDefault(emptyList())
                val defaultLocationId = currentUser.defaultLocationId
                val validDefaultLocationId = if (listAddress.any { it.id == defaultLocationId }) {
                    defaultLocationId
                } else {
                    null // Nếu không có trong danh sách thì để null
                }
                val currentLocationId = locationId ?: validDefaultLocationId

                val currentLocation = listAddress.find { it.id == currentLocationId }

                _uiState.update { it.copy(selectedLocation = currentLocation) }

                Log.d("CheckoutViewModel", "Loaded ${listAddress.size} addresses")
            } else {
                _uiState.update { it.copy(selectedLocation = null) }

                throw locationResult.exceptionOrNull()
                    ?: Exception("Lỗi không xác định khi tải danh sách địa chỉ")
            }
//--------------end d/c
//==============list ProductForCheckout
            val cart = repository.getCartByUser(currentUser.id)
            Log.d("CheckoutViewModel", "cart: $cart")
            if (cart == null) {
                Log.e("CheckoutViewModel", "Cart not found for UserId: ${currentUser.id}")
                _uiState.update {
                    it.copy(
                        errorMessage = "Cart not found for UserId: ${currentUser.id}"
                    )
                }
                return@launch
            }
            val productIds = cart.products.map { it.productId }
            val products = repository.getProductByListId(productIds).associateBy { it.id }

            val listProductToShow = cart.products.map { productOnCart ->
                val product = products[productOnCart.productId]

                if (product != null) {
                    val selectedOption = product.availableOptions?.listProductOptions
                        ?.find { it.optionsName == productOnCart.productOptions }
                    val price = selectedOption?.priceForOptions ?: product.price

                    val remainingStock = product.stockByVariant.find {
                        it.colorName == productOnCart.colorOptions && it.optionName == productOnCart.productOptions
                    }?.quantity ?: 0

                    val notEnough =
                        if (productOnCart.quantity > remainingStock) "sản phẩm trong kho không đủ" else ""

                    ProductsOnCartToShow(
                        productId = productOnCart.productId,
                        productName = productOnCart.productName,
                        productImage = productOnCart.productImage,
                        productPrice = price,
                        productOptions = productOnCart.productOptions,
                        colorOptions = productOnCart.colorOptions,
                        quantity = productOnCart.quantity,
                        remainingStock = remainingStock,
                        notExist = "",
                        notEnough = notEnough
                    )
                } else {
                    ProductsOnCartToShow(
                        productId = productOnCart.productId,
                        productName = productOnCart.productName,
                        productImage = productOnCart.productImage,
                        productPrice = 0.0,
                        productOptions = productOnCart.productOptions,
                        colorOptions = productOnCart.colorOptions,
                        quantity = productOnCart.quantity,
                        remainingStock = 0,
                        notExist = "Sản phẩm ngừng kinh doanh",
                        notEnough = "",
                    )
                }
            }
            val oldTotalPrice = listProductToShow.sumOf { it.productTotalPrice }
            _uiState.update {
                it.copy(
                    products = listProductToShow,
                    oldTotalPrice = oldTotalPrice,
                    totalPrice = oldTotalPrice,
                    finalPrice = oldTotalPrice + DataDummy.dummyShipping[2].price,
                    listShipping = DataDummy.dummyShipping,
                    selectedShipping = DataDummy.dummyShipping[2],
                )
            }

//----------------------done listProduct, oldTotalPrice, listShipping
//----------------------LoadCoupon

            val couponResult = repository.getActiveCoupons()
            couponResult.onSuccess { activeCoupons ->
                Log.d("CheckoutViewModel", "Số lượng coupon đang hoạt động: ${activeCoupons.size}")
                _uiState.update {
                    it.copy(
                        listCoupon = activeCoupons,
                    )
                }
            }.onFailure { e ->
                Log.e("CheckoutViewModel", "Lỗi khi tải coupons", e)
                _uiState.update { it.copy(errorMessage = e.localizedMessage) }
            }
//------------------------Done
        } catch (e: Exception) {
            Log.e("CheckoutViewModel", "Error loading Checkout: ${e.message}")
            _uiState.update {
                it.copy(
                    isButtonEnabled = false,
                    isLoading = false,
                    errorMessage = "Error loading Cart: ${e.message}"
                )
            }
        }
    }

    fun onChooseShipping() {
        _uiState.update {
            it.copy(
                isChooseShipping = true
            )
        }
    }

    fun shippingSelected(shipping: ShippingModel) {
        val previousShippingPrice = _uiState.value.selectedShipping?.price ?: 0.0
        val coupon = _uiState.value.selectedCoupon

        val newFinalPrice = if (coupon != null && coupon.type == CouponType.FREE_SHIPPING) {
            _uiState.value.finalPrice
        } else {
            _uiState.value.finalPrice - previousShippingPrice + shipping.price
        }

        _uiState.update {
            it.copy(
                finalPrice = newFinalPrice,
                selectedShipping = shipping
            )
        }
    }


    fun onConfirmationShipping() {
        _uiState.update {
            it.copy(
                isChooseShipping = false
            )
        }
    }

    fun onChooseCoupon() {
        _uiState.update {
            it.copy(
                isChooseCoupon = true
            )
        }
    }

    fun couponSelected(coupon: CouponModel) {
        val oldTotalPrice = _uiState.value.oldTotalPrice
        // Đảm bảo totalPrice không bị âm
        val totalPrice = when (coupon.type) {
            CouponType.PERCENTAGE -> (oldTotalPrice - (oldTotalPrice * coupon.value)).coerceAtLeast(0.0)
            CouponType.FIXED_AMOUNT -> (oldTotalPrice - coupon.value).coerceAtLeast(0.0)
            else -> oldTotalPrice
        }

        val finalPrice = if(coupon.type!=CouponType.FREE_SHIPPING){
            totalPrice + (_uiState.value.selectedShipping?.price ?: 0.0)
        }else{
            totalPrice
        }


        _uiState.update {
            it.copy(
                selectedCoupon = coupon,
                totalPrice = totalPrice,
                finalPrice = finalPrice
            )
        }
    }

    fun onConfirmationCoupon() {
        _uiState.update {
            it.copy(
                isChooseCoupon = false
            )
        }
    }

}
