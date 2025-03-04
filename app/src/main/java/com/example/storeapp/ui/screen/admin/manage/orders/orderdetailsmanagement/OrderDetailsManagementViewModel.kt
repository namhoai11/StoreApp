package com.example.storeapp.ui.screen.admin.manage.orders.orderdetailsmanagement

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import com.example.storeapp.model.OrderStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderDetailsManagementViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: FirebaseFireStoreRepository

) : ViewModel() {
    private val _uiState = MutableStateFlow(OrderDetailsManagementUiState())
    val uiState: StateFlow<OrderDetailsManagementUiState> = _uiState

    private val orderId: String? = savedStateHandle["orderId"]

    init {
        loadOrder()
        viewModelScope.launch {
            _uiState.collect { state ->
                Log.d("OrderDetailsManagementViewModel", "Current UI State: $state")
            }
        }
    }

    private fun loadOrder() {
        viewModelScope.launch {
            if (orderId.isNullOrEmpty()) {
                Log.w("OrderDetailsManagementViewModel", "No orderId provided, skipping load.")
                return@launch
            }
            _uiState.update { it.copy(isLoading = true) }
            val result = repository.getOrderById(orderId)
            result.onSuccess { order ->
                val userResult = runCatching { repository.getUserById(order.userId) }.getOrNull()

                val currentButtonText = when (order.status) {
                    OrderStatus.PENDING -> "Xác nhận đơn hàng"
                    OrderStatus.CONFIRMED -> "Giao hàng"
                    OrderStatus.SHIPPED -> "Hoàn thành"
                    OrderStatus.ALL -> ""
                    OrderStatus.AWAITING_PAYMENT -> "Thanh toán"
                    OrderStatus.COMPLETED -> "Đã hoàn thành"
                    OrderStatus.CANCELED -> "Đã hủy"
                }
                _uiState.update {
                    it.copy(
                        order = order,
                        user = userResult,
                        currentButtonText = currentButtonText,
                        isLoading = false
                    )
                }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
            }
        }
    }

    fun cancelOrderClicked() {
        viewModelScope.launch {
            val result =
                repository.updateOrderStatus(_uiState.value.order.orderCode, OrderStatus.CANCELED)
            result.onSuccess {
                Log.d("OrderDetailsManagementViewModel", "Hủy đơn hàng thành công!")
                loadOrder()
                // Cập nhật UI nếu cần
            }.onFailure {
                Log.e("OrderDetailsManagementViewModel", "Lỗi khi hủy đơn hàng: ${it.message}")
            }
        }
    }

    fun completedOrderClicked() {

        _uiState.update {
            it.copy(
                isLoading = true
            )
        }
        val orderStatus = when (_uiState.value.order.status) {
            OrderStatus.COMPLETED -> OrderStatus.COMPLETED
            OrderStatus.ALL -> OrderStatus.ALL
            OrderStatus.AWAITING_PAYMENT -> OrderStatus.PENDING
            OrderStatus.PENDING -> OrderStatus.CONFIRMED
            OrderStatus.CONFIRMED -> OrderStatus.SHIPPED
            OrderStatus.SHIPPED -> OrderStatus.COMPLETED
            OrderStatus.CANCELED -> OrderStatus.CANCELED
        }
        viewModelScope.launch {
            val result =
                repository.updateOrderStatus(_uiState.value.order.orderCode, orderStatus)
            result.onSuccess {
                Log.d("OrderDetailsManagementViewModel", "Cap nhat thành công!")
                _uiState.update {
                    it.copy(
                        isLoading = false
                    )
                }
                loadOrder()
                // Cập nhật UI nếu cần
            }.onFailure {
                Log.e(
                    "OOrderDetailsManagementViewModel",
                    "Lỗi khi cap nhat trang thai don hang: ${it.message}"
                )
            }
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

    fun onChooseCompletedOrder() {
        _uiState.update {
            it.copy(
                isShowCompletedOrderDialog = true,
            )
        }
    }

    fun dismissCompletedOrderDialog() {
        _uiState.update {
            it.copy(
                isShowCompletedOrderDialog = false,
            )
        }
    }
}