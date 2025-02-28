package com.example.storeapp.ui.screen.order

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import com.example.storeapp.model.OrderStatus
import com.example.storeapp.model.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderViewModel(
    private val repository: FirebaseFireStoreRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()

    private val _user = MutableLiveData<UserModel?>()
    val user: LiveData<UserModel?> = _user

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
            try {
                val userData = repository.getCurrentUser()
                _user.value = userData
                Log.d("OrderViewModel", "User loaded: $userData")

                loadData()

            } catch (e: Exception) {
                Log.e("OrderViewModel", "Error loading user", e)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Lỗi khi tải người dùng"
                    )
                }
            }
        }
    }

    fun loadData() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }

        val currentUser = _user.value
        if (currentUser == null) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "Không thể xác định người dùng"
                )
            }
            return@launch
        }

        val resultLoadOrder = repository.getOrdersByUser(currentUser.id)
        resultLoadOrder.onSuccess { orders ->
            if (orders.isNotEmpty()) {
                val listOrderByStatus = orders.groupBy { it.status }
                _uiState.update {
                    it.copy(
                        listOrder = orders,
                        listOrderByStatus = listOrderByStatus,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        listOrder = emptyList(),
                        listOrderByStatus = emptyMap(),
                        isLoading = false,
                        errorMessage = "Không có đơn hàng nào"
                    )
                }
            }
        }.onFailure { exception ->
            Log.e("OrderViewModel", "Error loading orders", exception)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "Lỗi khi tải đơn hàng: ${exception.message}"
                )
            }
        }
    }

    fun selectOrderStatus(status: OrderStatus) {
        _uiState.update { it.copy(currentOrderStatus = status) }
    }


    // Hàm tìm kiếm sản phẩm theo tên
    fun searchOrdersByCode(query: String) = viewModelScope.launch {
        _uiState.update { currentState ->
            val filteredOrders = if (query.isNotBlank()) {
                currentState.currentListOrder.filter { it.orderCode.contains(query, ignoreCase = true) }
            } else {
                currentState.currentListOrder
            }
            currentState.copy(
                ordersSearched = filteredOrders,
                currentQuery = query
            )
        }
        Log.d("OrderViewModel", "itemsSearched:${_uiState.value.ordersSearched}")
    }
}