package com.example.storeapp.ui.screen.admin.manage.coupon.add_coupon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import com.example.storeapp.model.CouponType
import com.example.storeapp.ui.component.function.toTimestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddCouponViewModel(
    private val repository: FirebaseFireStoreRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddCouponUiState())
    val uiState: StateFlow<AddCouponUiState> = _uiState.asStateFlow()

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(couponDetailsItem = it.couponDetailsItem.copy(name = newName)) }
    }

    //    fun onQuantityChange(newQuantity: String) {
//        val quantity = newQuantity.toIntOrNull() ?: 1
//        _uiState.update { it.copy(couponDetailsItem = it.couponDetailsItem.copy(quantity = quantity)) }
//    }
    fun onQuantityChange(newQuantity: String) {
        _uiState.update { it.copy(quantityInput = newQuantity) }
    }


    fun onTypeSelected(newType: CouponType) {
        _uiState.update { it.copy(couponDetailsItem = it.couponDetailsItem.copy(type = newType)) }
    }

    //    fun onValueChange(newValue: String) {
//        val value = newValue.toDoubleOrNull() ?: 0.0
//        _uiState.update { it.copy(couponDetailsItem = it.couponDetailsItem.copy(value = value)) }
//    }
    fun onValueChange(newValue: String) {
        _uiState.update { it.copy(valueInput = newValue) }
    }

    fun onStartDateChange(newDate: String) {
        val timestamp = newDate.toTimestamp() // Chuyển chuỗi thành Timestamp
        _uiState.update { it.copy(couponDetailsItem = it.couponDetailsItem.copy(startDate = timestamp)) }
    }

    fun onEndDateChange(newDate: String) {
        val timestamp = newDate.toTimestamp()
        _uiState.update { it.copy(couponDetailsItem = it.couponDetailsItem.copy(endDate = timestamp)) }
    }

    fun onDescriptionChange(newDescription: String) {
        _uiState.update { it.copy(couponDetailsItem = it.couponDetailsItem.copy(description = newDescription)) }
    }

    fun addCoupon(navigation: () -> Unit) {
        if (!validateCoupon()) return // Nếu không hợp lệ, dừng lại

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) } // Hiển thị loading

            val quantity = _uiState.value.quantityInput.toInt()
            val value = _uiState.value.valueInput.toDouble()

            val updatedCoupon = _uiState.value.couponDetailsItem.copy(
                quantity = quantity,
                value = value
            )

            val result = repository.addCouponToFireStore(updatedCoupon)

            result.onSuccess {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Thêm coupon thành công!"
                    )
                }
                navigation()

            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
            }
        }
    }


    private fun validateCoupon(): Boolean {
        val uiState = _uiState.value
        val coupon = uiState.couponDetailsItem

        if (coupon.name.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Tên không được để trống") }
            return false
        }

        val quantity = uiState.quantityInput.toIntOrNull()
        if (quantity == null || quantity <= 0) {
            _uiState.update { it.copy(errorMessage = "Số lượng phải là số nguyên dương") }
            return false
        }

        val value = uiState.valueInput.toDoubleOrNull()
        if (value == null || value <= 0) {
            _uiState.update { it.copy(errorMessage = "Giá trị phải là số dương") }
            return false
        }

        if (coupon.startDate >= coupon.endDate) {
            _uiState.update { it.copy(errorMessage = "Ngày bắt đầu phải nhỏ hơn ngày kết thúc") }
            return false
        }

        if (coupon.description.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Mô tả không được để trống") }
            return false
        }

        // Nếu hợp lệ, xóa lỗi cũ
        _uiState.update { it.copy(errorMessage = null) }
        return true
    }


}
