package com.example.storeapp.ui.screen.address.add_address

import com.example.storeapp.model.District
import com.example.storeapp.model.Province
import com.example.storeapp.model.Ward


data class AddAddressUiState(
    val provinces: List<Province> = emptyList(),
    val districts: List<District> = emptyList(),
    val wards: List<Ward> = emptyList(),
    val selectedProvince: Province? = null,
    val selectedDistrict: District? = null,
    val selectedWard: Ward? = null,
    val street: String = "",
    val isLoading: Boolean = false,  // Xử lý trạng thái tải API
    val errorMessage: String? = null, // Xử lý lỗi nếu có
    val successMessage: String = "",
)