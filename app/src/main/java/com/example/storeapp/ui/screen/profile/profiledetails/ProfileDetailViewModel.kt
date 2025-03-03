package com.example.storeapp.ui.screen.profile.profiledetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import com.example.storeapp.model.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileDetailViewModel(
    private val repository: FirebaseFireStoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileDetailUiState())
    val uiState: StateFlow<ProfileDetailUiState> = _uiState

    private val _user = MutableLiveData<UserModel?>()
    val user: LiveData<UserModel?> = _user

    init {
        loadUser()

        // Theo dõi UI state
        viewModelScope.launch {
            _uiState.collect { state ->
                Log.d("ProfileDetailViewModel", "Current UI State: $state")
            }
        }
    }

    fun loadUser() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val userData = repository.getCurrentUser()
                if (userData != null) {
                    _user.postValue(userData)
                    Log.d("ProfileDetailViewModel", "User loaded: $userData")

                    loadData(userData.id)
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Không tìm thấy người dùng"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("ProfileDetailViewModel", "Error loading user", e)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Lỗi khi tải người dùng"
                    )
                }
            }
        }
    }

    private fun loadData(userId: String) = viewModelScope.launch {
        try {
            val resultLoadOrder = repository.getOrdersByUser(userId)

            resultLoadOrder.onSuccess { orders ->
                val sumTotalPrice = orders.sumOf { it.totalPrice }
                _uiState.update {
                    it.copy(
                        userSpend = sumTotalPrice,
                        isLoading = false
                    )
                }
            }.onFailure { exception ->
                Log.e("ProfileDetailViewModel", "Error loading orders", exception)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Lỗi khi tải đơn hàng: ${exception.message}"
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("ProfileDetailViewModel", "Unexpected error", e)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "Lỗi không xác định: ${e.message}"
                )
            }
        }
    }
}
