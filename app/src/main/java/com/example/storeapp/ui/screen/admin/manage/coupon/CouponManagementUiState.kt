package com.example.storeapp.ui.screen.admin.manage.coupon

import com.example.storeapp.model.CouponActive
import com.example.storeapp.model.CouponModel

data class CouponManagementUiState(
    val listCoupon: List<CouponModel> = emptyList(),
//    val couponSelected: CouponModel = CouponModel(),
    val currentCouponActive: CouponActive = CouponActive.ALL,
    val listCouponByActive: Map<CouponActive, List<CouponModel>> = emptyMap(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
) {
    val currentListCoupon: List<CouponModel>
        get() = when (currentCouponActive) {
            CouponActive.ALL -> listCoupon
            else -> listCouponByActive[currentCouponActive] ?: emptyList()
        }
}
