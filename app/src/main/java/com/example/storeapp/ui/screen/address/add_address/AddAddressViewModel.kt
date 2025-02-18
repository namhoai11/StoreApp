package com.example.storeapp.ui.screen.address.add_address

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.model.District
import com.example.storeapp.model.Province
import com.example.storeapp.model.Ward
import com.example.storeapp.network.LocationApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddAddressViewModel(
    private val locationApiService: LocationApiService
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddAddressUiState(isLoading = true))
    val uiState: StateFlow<AddAddressUiState> = _uiState.asStateFlow()

    init {
        loadProvinces()
    }

    private fun loadProvinces() {
        viewModelScope.launch {
            try {
                val provinces = locationApiService.getProvinces()
                _uiState.update { it.copy(provinces = provinces, isLoading = false) }
                Log.d("AddAddressViewModel","provinces: $provinces")

            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Không thể tải dữ liệu: ${e.message}", isLoading = false) }
                Log.e("AddAddressViewModel", "Lỗi tải dữ liệu: ", e)
            }
        }
    }

    fun onProvinceSelected(province: Province) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(selectedProvince = province, isLoading = true) }
                val provinceData = locationApiService.getDistricts(province.code)
                _uiState.update {
                    it.copy(districts = provinceData.districts, selectedDistrict = null, selectedWard = null, isLoading = false)
                }
                Log.d("AddAddressViewModel","provinceData: $provinceData")

            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Lỗi tải huyện: ${e.message}", isLoading = false) }
                Log.e("AddAddressViewModel", "Lỗi tải huyen: ", e)

            }
        }
    }

    fun onDistrictSelected(district: District) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(selectedDistrict = district, isLoading = true) }
                val districtData = locationApiService.getWards(district.code)
                _uiState.update {
                    it.copy(wards = districtData.wards, selectedWard = null, isLoading = false)
                }
                Log.d("AddAddressViewModel","districtData: $districtData")

            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Lỗi tải xã: ${e.message}", isLoading = false) }
                Log.e("AddAddressViewModel", "Lỗi tải xa: ", e)
            }
        }
    }

    fun onWardSelected(ward: Ward) {
        _uiState.update { it.copy(selectedWard = ward) }
        Log.d("AddAddressViewModel","ward: $ward")
        Log.d("AddAddressViewModel","selectedProvince: ${_uiState.value.selectedProvince}")
        Log.d("AddAddressViewModel","selectedDistrict: ${_uiState.value.selectedDistrict}")
        Log.d("AddAddressViewModel","selectedWard: ${_uiState.value.selectedWard}")
    }

    fun onStreetInput(street: String) {
        _uiState.update { it.copy(street = street) }
    }

    fun onConfirm() {
        // TODO: Thực hiện logic khi xác nhận địa chỉ
    }
}

