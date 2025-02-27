package com.example.storeapp.ui.screen.checkout.successpayment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import com.example.storeapp.model.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SuccessPaymentViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: FirebaseFireStoreRepository

) : ViewModel() {
    private val _uiState = MutableStateFlow(SuccessPaymentUiState())
    val uiState: StateFlow<SuccessPaymentUiState> = _uiState

    private val _user = MutableLiveData<UserModel?>()
    val user: LiveData<UserModel?> = _user

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
                Log.w("PaymentViewModel", "No orderId provided, skipping load.")
                return@launch
            }
            _uiState.update { it.copy(isLoading = true) }
            val result = repository.getOrderById(orderId)
            result.onSuccess { order ->
                _uiState.update {
                    it.copy(
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
}