package com.example.storeapp.ui.screen.admin.dashboard

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
import java.util.Calendar

class DashBoardViewModel(
    private val repository: FirebaseFireStoreRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DashBoardUiState())
    val uiState: StateFlow<DashBoardUiState> = _uiState.asStateFlow()

    private val _user = MutableLiveData<UserModel?>()
    val user: LiveData<UserModel?> = _user

    init {
        loadUser()
        loadData()
        viewModelScope.launch {
            _uiState.collect { state ->
                Log.d("DashBoardViewModel", "Current UI State: $state")
            }
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            val userData = repository.getCurrentUser()
            _user.value = userData
            Log.d("HomeViewModel", "User loaded: $userData")
        }
    }

    fun loadData() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        val resultLoadOrder = repository.getAllOrders()

        resultLoadOrder.onSuccess { orders ->
            if (orders.isNotEmpty()) {
                val now = Calendar.getInstance()
                val currentMonth = now.get(Calendar.MONTH) + 1 // Calendar.MONTH bắt đầu từ 0
                val currentYear = now.get(Calendar.YEAR)
                val currentDay = now.get(Calendar.DAY_OF_MONTH)

                // Lọc đơn hàng trong tháng hiện tại
                val listOrderOfMonth = orders.filter { order ->
                    val createdAt = Calendar.getInstance().apply { time = order.createdAt.toDate() }
                    createdAt.get(Calendar.MONTH) + 1 == currentMonth && createdAt.get(Calendar.YEAR) == currentYear
                }

                // Lọc đơn hàng chưa hoàn thành hoặc chưa bị hủy
                val listOrderProcessing = orders.filter { order ->
                    order.status != OrderStatus.COMPLETED && order.status != OrderStatus.CANCELED
                }

                // Lọc đơn hàng mới nhất
                val listNewOrder = orders.sortedByDescending { it.createdAt }.take(10)

                // Tính doanh thu trong tháng
                val revenueThisMonth = listOrderOfMonth.sumOf { it.totalPrice }

                // Tính doanh thu trong ngày
                val revenueThisDay = orders
                    .filter {
                        val createdAt = Calendar.getInstance().apply { time = it.createdAt.toDate() }
                        createdAt.get(Calendar.DAY_OF_MONTH) == currentDay &&
                                createdAt.get(Calendar.MONTH) + 1 == currentMonth &&
                                createdAt.get(Calendar.YEAR) == currentYear
                    }
                    .sumOf { it.totalPrice }

                _uiState.update {
                    it.copy(
                        allOrder = orders,
                        listOrderOfMonth = listOrderOfMonth,
                        listOrderProcessing = listOrderProcessing,
                        listNewOrder = listNewOrder,
                        revenueThisMonth = revenueThisMonth,
                        revenueThisDay = revenueThisDay,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        allOrder = emptyList(),
                        listOrderOfMonth = emptyList(),
                        listOrderProcessing = emptyList(),
                        listNewOrder = emptyList(),
                        revenueThisMonth = 0.0,
                        revenueThisDay = 0.0,
                        isLoading = false,
                        errorMessage = "Không có đơn hàng nào"
                    )
                }
            }
        }.onFailure { exception ->
            Log.e("DashBoardViewModel", "Error loading orders", exception)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "Lỗi khi tải đơn hàng: ${exception.message}"
                )
            }
        }
    }
}
