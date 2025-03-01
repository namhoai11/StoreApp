package com.example.storeapp.ui.screen.admin.manage.orders

import android.util.Log
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

class OrderManagementViewModel(
    private val repository: FirebaseFireStoreRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(OrderManagementUiState())
    val uiState: StateFlow<OrderManagementUiState> = _uiState.asStateFlow()

    init {
        loadData()
        viewModelScope.launch {
            _uiState.collect { state ->
                Log.d("OrderManagementViewModel", "Current UI State: $state")
            }
        }
    }

    fun loadData() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }

        val resultLoadOrder = repository.getAllOrders()
        resultLoadOrder.onSuccess { orders ->
            if (orders.isNotEmpty()) {
                // Lấy danh sách userId duy nhất từ danh sách đơn hàng
                val userIds = orders.map { it.userId }.toSet()

                // Lấy thông tin người dùng từ Firestore
                val userMap = mutableMapOf<String, UserModel>()
                userIds.forEach { userId ->
                    val userResult = repository.getUserById(userId)
                    userMap[userId] = userResult
                }

                val listOrderByStatus = orders.groupBy { it.status }

                _uiState.update {
                    it.copy(
                        allOrder = orders,
                        ordersByStatus = listOrderByStatus,
                        userMap = userMap, // Lưu thông tin User riêng biệt
                        isLoading = false
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        allOrder = emptyList(),
                        ordersByStatus = emptyMap(),
                        isLoading = false,
                        errorMessage = "Không có đơn hàng nào"
                    )
                }
            }
        }.onFailure { exception ->
            Log.e("OrderManagementViewModel", "Error loading orders", exception)
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
                currentState.allOrder.filter { it.orderCode.contains(query, ignoreCase = true) }
            } else {
                currentState.allOrder
            }
            currentState.copy(
                ordersSearched = filteredOrders,
                currentQuery = query
            )
        }
        Log.d("OrderManagementViewModel", "itemsSearched:${_uiState.value.ordersSearched}")
    }

}