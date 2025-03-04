package com.example.storeapp.ui.screen.admin.manage.user.userdetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import com.example.storeapp.model.OrderStatus
import com.example.storeapp.model.Role
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserDetailsManagementViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: FirebaseFireStoreRepository

) : ViewModel() {
    private val _uiState = MutableStateFlow(UserDetailsManagementUiState())
    val uiState: StateFlow<UserDetailsManagementUiState> = _uiState

    private val userId: String? = savedStateHandle["userId"]

    init {
        loadData()
        viewModelScope.launch {
            _uiState.collect { state ->
                Log.d("UserDetailsManagementViewModel", "Current UI State: $state")
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            if (userId.isNullOrEmpty()) {
                Log.w("UserDetailsManagementViewModel", "No userId provided, skipping load.")
                return@launch
            }

            _uiState.update { it.copy(isLoading = true) }

            try {
                // Load thông tin người dùng
                val user = repository.getUserById(userId)
                if (user != null) {
                    val currentButtonText = when (user.role) {
                        Role.ADMIN -> "Xóa quyền quản trị viên"
                        Role.USER -> "Cấp quyền quản trị viên"
                    }

                    _uiState.update {
                        it.copy(
                            user = user,
                            currentButtonText = currentButtonText
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "Người dùng không tồn tại.")
                    }
                    return@launch
                }

                // Load danh sách đơn hàng
                val resultLoadOrder = repository.getOrdersByUser(userId)
                resultLoadOrder.onSuccess { orders ->
                    val validOrders = orders.filter { it.status != OrderStatus.CANCELED }

                    val sumTotalPrice = validOrders.sumOf { it.totalPrice }
                    _uiState.update {
                        it.copy(userSpend = sumTotalPrice)
                    }
                }.onFailure { exception ->
                    Log.e("UserDetailsManagementViewModel", "Error loading orders", exception)
                    _uiState.update {
                        it.copy(errorMessage = "Lỗi khi tải đơn hàng: ${exception.message}")
                    }
                }

                _uiState.update { it.copy(isLoading = false) }

            } catch (e: Exception) {
                Log.e("UserDetailsManagementViewModel", "Unexpected error", e)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Lỗi không xác định: ${e.message}"
                    )
                }
            }
        }
    }

    fun confirmSetUserRoleClicked() {
        _uiState.update {
            it.copy(
                isLoading = true
            )
        }
        val newUserRole = when (_uiState.value.user.role) {
            Role.USER -> Role.ADMIN
            Role.ADMIN -> Role.USER
        }
        viewModelScope.launch {
            val result =
                repository.updateUserRole(_uiState.value.user.id, newUserRole)
            result.onSuccess {
                Log.d("UserDetailsManagementViewModel", "Cap nhat thành công!")
                _uiState.update {
                    it.copy(
                        isLoading = false
                    )
                }
                loadData()
                // Cập nhật UI nếu cần
            }.onFailure {
                Log.e(
                    "UserDetailsManagementViewModel",
                    "Lỗi khi cap nhat trang thai don hang: ${it.message}"
                )
            }
        }
    }

    fun onChooseSetRoleUser() {
        _uiState.update {
            it.copy(
                isShowSetRoleDialog = true,
            )
        }
    }

    fun dismissChooseSetRoleUser() {
        _uiState.update {
            it.copy(
                isShowSetRoleDialog = false,
            )
        }
    }

}