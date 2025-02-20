package com.example.storeapp.ui.screen.admin.manage.coupon.add_coupon

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import com.example.storeapp.model.CouponModel
import com.example.storeapp.model.CouponType
import com.example.storeapp.ui.component.function.toTimestamp
import com.example.storeapp.ui.screen.cart.ProductsOnCartToShow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddCouponViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: FirebaseFireStoreRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddCouponUiState())
    val uiState: StateFlow<AddCouponUiState> = _uiState.asStateFlow()

    private val couponId: String? = savedStateHandle["couponId"]
    private val isEditing: Boolean = savedStateHandle["isEditing"] ?: false

    fun loadCoupon() {
        viewModelScope.launch {
            if (couponId.isNullOrEmpty()) {
                Log.w("AddCouponViewModel", "No couponId provided, skipping load.")
                return@launch // Không cần load nếu không có couponId
            }
            _uiState.update { it.copy(isLoading = true) }
            val result = repository.getCouponById(couponId)
            result.onSuccess { coupon ->
                _uiState.update {
                    it.copy(
                        couponDetailsItem = coupon,
                        quantityInput = coupon.quantity.toString(),
                        valueInput = coupon.value.toString(),
                        isEditing = isEditing,
                        isLoading = false
                    )
                }
                Log.d("AddCouponViewModel", "Coupon Loaded: ${_uiState.value.couponDetailsItem}")
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
            }
        }
    }

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

    // Định nghĩa trạng thái mặc định

    fun addOrUpdateCoupon() {
        if (!validateCoupon()) return // Nếu không hợp lệ, dừng lại

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) } // Hiển thị loading

            val quantity = _uiState.value.quantityInput.toIntOrNull() ?: 0
            val value = _uiState.value.valueInput.toDoubleOrNull() ?: 0.0

            val updatedCoupon = _uiState.value.couponDetailsItem.copy(
                quantity = quantity,
                value = value
            )

            val result = repository.addOrUpdateCouponToFireStore(updatedCoupon)

            result.onSuccess {
                _uiState.update {
                    it.copy(
                        successMessage = "Thêm coupon thành công!",
                        isEditing = false
                    )
                }

                Log.d("AddCouponUiState", "defaultUiState:${_uiState.value}")
//                navigation()
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

    fun editCouponClicked() {
        _uiState.update { it.copy(isEditing = true) }
    }

    fun removeCoupon(navigation:()->Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = "") }

            val couponId = _uiState.value.couponDetailsItem.id
            if (couponId.isBlank()) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Coupon ID không hợp lệ") }
                return@launch
            }

            val result = repository.removeCouponById(couponId)
            result.onSuccess {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Coupon đã được xóa thành công!",
                        couponDetailsItem = CouponModel(), // Reset data
                        quantityInput = "",
                        valueInput = ""
                    )
                }
                navigation()

            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
            }
        }
    }

}
