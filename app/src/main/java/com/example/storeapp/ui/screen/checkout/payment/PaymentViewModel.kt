package com.example.storeapp.ui.screen.checkout.payment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.local.DataDummy
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import com.example.storeapp.model.PaymentMethodModel
import com.example.storeapp.model.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PaymentViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: FirebaseFireStoreRepository

) : ViewModel() {
    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState

    private val _user = MutableLiveData<UserModel?>()
    val user: LiveData<UserModel?> = _user

    private val orderId: String? = savedStateHandle["orderId"]


    init {
        loadUser()
        viewModelScope.launch {
            _uiState.collect { state ->
                Log.d("PaymentViewModel", "Current UI State: $state")
            }
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            val userData = repository.getCurrentUser()
            _user.value = userData
            Log.d("PaymentViewModel", "User loaded: $userData")
            loadOrder()
        }
    }

    private fun loadOrder() {
        viewModelScope.launch {
            if (orderId.isNullOrEmpty()) {
                Log.w("PaymentViewModel", "No orderId provided, skipping load.")
                return@launch
            }
            _uiState.update { it.copy(isLoading = true) }
            val result = repository.getOrderById(orderId)
            result.onSuccess { order ->
                _uiState.update {
                    it.copy(
                        listPaymentMethodModel = DataDummy.dummyPaymentMethod,
                        currentOrder = order,
                        isLoading = false
                    )
                }
                Log.d("PaymentViewModel", "Order Loaded: ${_uiState.value.currentOrder}")
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
            }
        }
    }

    fun onChooseCardPayment(paymentMethod: PaymentMethodModel) {
        _uiState.update {
            it.copy(
                paymentMethodSelected = paymentMethod,
                isButtonEnabled = true
            )
        }
    }

    fun onChoosePaymentMethod() {
        _uiState.update {
            it.copy(
                isShowDialog = true
            )
        }
    }

    fun dismissDialog() {
        _uiState.update {
            it.copy(
                isShowDialog = false,
            )
        }
    }

    fun confirmPaymentClicked(navigate: (String) -> Unit) {
        val orderCode = _uiState.value.currentOrder.orderCode
        val paymentMethod = _uiState.value.paymentMethodSelected?.name

        if (paymentMethod == null) {
            Log.e("PaymentViewModel", "Lỗi: Thiếu thông tin đơn hàng hoặc phương thức thanh toán")
            return
        }
        viewModelScope.launch {
            try {
                val result = repository.updateOrderPaymentMethod(orderCode, paymentMethod)
                result.onSuccess {
                    Log.d("PaymentViewModel", "Cập nhật phương thức thanh toán thành công!")
                    navigate(orderCode)
                }.onFailure {
                    Log.e("PaymentViewModel", "Lỗi: ${it.message}")
                }
            } catch (e: Exception) {
                Log.e("PaymentViewModel", "Lỗi ngoại lệ khi cập nhật phương thức thanh toán", e)
            }
        }
    }


}