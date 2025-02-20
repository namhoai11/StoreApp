package com.example.storeapp.ui.screen.admin.manage.coupon.add_coupon

import com.example.storeapp.model.CouponModel

data class AddCouponUiState(
    val couponDetailsItem: CouponModel = CouponModel(),
    val quantityInput: String = "",
    val valueInput: String = "",
    val isEditing: Boolean = false,
    val isLoading: Boolean = false,  // Xử lý trạng thái tải API
    val errorMessage: String? = null, // Xử lý lỗi nếu có
    val successMessage: String = "",
)