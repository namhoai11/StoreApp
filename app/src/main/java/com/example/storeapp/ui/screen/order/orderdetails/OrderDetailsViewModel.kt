package com.example.storeapp.ui.screen.order.orderdetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: FirebaseFireStoreRepository

) : ViewModel(){
    private val _uiState = MutableStateFlow(OrderDetailsUiState())
    val uiState: StateFlow<OrderDetailsUiState> = _uiState

    private val orderId: String? = savedStateHandle["orderId"]

    init {
        loadOrder()
        viewModelScope.launch {
            _uiState.collect { state ->
                Log.d("PaymentViewModel", "Current UI State: $state")
            }
        }
    }

    private fun loadOrder() {
        viewModelScope.launch {
            if (orderId.isNullOrEmpty()) {
                Log.w("OrderDetailsViewModel", "No orderId provided, skipping load.")
                return@launch
            }
            _uiState.update { it.copy(isLoading = true) }
            val result = repository.getOrderById(orderId)
            result.onSuccess { order ->
                _uiState.update {
                    it.copy(
                        order = order,
                        isLoading = false
                    )
                }
                Log.d("OrderDetailsViewModel", "Order Loaded: ${_uiState.value.order}")
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
            }
        }
    }

    fun cancelOrderClicked() {
        viewModelScope.launch {
            val result = repository.cancelOrder(_uiState.value.order.orderCode)
            result.onSuccess {
                Log.d("OrderViewModel", "Hủy đơn hàng thành công!")
                // Cập nhật UI nếu cần
            }.onFailure {
                Log.e("OrderViewModel", "Lỗi khi hủy đơn hàng: ${it.message}")
            }
        }
    }

    fun onChoosePayment() {
        _uiState.update {
            it.copy(
                isShowPaymentDialog = true,
            )
        }
    }

    fun dismissPaymentDialog() {
        _uiState.update {
            it.copy(
                isShowPaymentDialog = false,
            )
        }
    }

    fun onChooseCanceledOrder() {
        _uiState.update {
            it.copy(
                isShowCanceledOrderDialog = true,
            )
        }
    }

    fun dismissCanceledOrderDialog() {
        _uiState.update {
            it.copy(
                isShowCanceledOrderDialog = false,
            )
        }
    }

}