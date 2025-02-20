package com.example.storeapp.ui.screen.admin.manage.coupon

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import com.example.storeapp.model.CouponActive
import com.example.storeapp.model.CouponModel
import com.example.storeapp.ui.screen.admin.manage.coupon.add_coupon.AddCouponViewModel
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CouponManagementViewModel(private val repository: FirebaseFireStoreRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(CouponManagementUiState())
    val uiState: StateFlow<CouponManagementUiState> = _uiState.asStateFlow()

    init {
        loadCoupons()
    }

    fun loadCoupons() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = repository.getCoupons()
            result.onSuccess { coupons ->
                Log.d("CouponViewModel", "Số lượng coupon tải về: ${coupons.size}")

                val now = Timestamp.now()
                val updatedCoupons = coupons.map {
                    val status = when {
                        it.endDate < now -> CouponActive.EXPIRED
                        it.startDate > now -> CouponActive.UPCOMING
                        else -> CouponActive.ONGOING
                    }
                    Log.d("CouponViewModel", "Coupon ${it.id} - ${it.name} có trạng thái: $status")
                    it.copy(active = status)
                }

                val categorizedCoupons =
                    updatedCoupons.groupBy { it.active }.withDefault { emptyList() }

                Log.d("CouponViewModel", "Coupons theo trạng thái: $categorizedCoupons")

                _uiState.update {
                    it.copy(
                        listCoupon = updatedCoupons,
                        listCouponByActive = categorizedCoupons,
                        isLoading = false
                    )
                }
            }.onFailure { e ->
                Log.e("CouponViewModel", "Lỗi khi tải coupons", e)
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
            }
        }
    }


    fun selectActive(active: String) {
        _uiState.update { it.copy(currentCouponActive = CouponActive.fromString(active)!!) }
    }


}
