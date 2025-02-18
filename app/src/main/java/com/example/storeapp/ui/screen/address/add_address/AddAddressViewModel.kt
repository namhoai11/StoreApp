package com.example.storeapp.ui.screen.address.add_address

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import com.example.storeapp.model.District
import com.example.storeapp.model.Province
import com.example.storeapp.model.UserLocationModel
import com.example.storeapp.model.UserModel
import com.example.storeapp.model.Ward
import com.example.storeapp.network.LocationApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddAddressViewModel(
    private val repository: FirebaseFireStoreRepository,
    private val locationApiService: LocationApiService
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddAddressUiState(isLoading = true))
    val uiState: StateFlow<AddAddressUiState> = _uiState.asStateFlow()

    private val _user = MutableLiveData<UserModel?>()
    val user: LiveData<UserModel?> = _user

    init {
        loadUser()
        loadProvinces()
    }

    private fun loadUser() {
        viewModelScope.launch {
            val userData = repository.getCurrentUser()
            _user.value = userData
            Log.d("ProductDetailsViewmodel", "User loaded: $userData")

        }
    }

    private fun loadProvinces() {
        viewModelScope.launch {
            try {
                val provinces = locationApiService.getProvinces()
                _uiState.update { it.copy(provinces = provinces, isLoading = false) }
                Log.d("AddAddressViewModel", "provinces: $provinces")

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Không thể tải dữ liệu: ${e.message}",
                        isLoading = false
                    )
                }
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
                    it.copy(
                        districts = provinceData.districts,
                        selectedDistrict = null,
                        selectedWard = null,
                        isLoading = false
                    )
                }
                Log.d("AddAddressViewModel", "provinceData: $provinceData")

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Lỗi tải huyện: ${e.message}",
                        isLoading = false
                    )
                }
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
                Log.d("AddAddressViewModel", "districtData: $districtData")

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Lỗi tải xã: ${e.message}",
                        isLoading = false
                    )
                }
                Log.e("AddAddressViewModel", "Lỗi tải xa: ", e)
            }
        }
    }

    fun onWardSelected(ward: Ward) {
        _uiState.update { it.copy(selectedWard = ward) }
        Log.d("AddAddressViewModel", "ward: $ward")
        Log.d("AddAddressViewModel", "selectedProvince: ${_uiState.value.selectedProvince}")
        Log.d("AddAddressViewModel", "selectedDistrict: ${_uiState.value.selectedDistrict}")
        Log.d("AddAddressViewModel", "selectedWard: ${_uiState.value.selectedWard}")
    }

    fun onStreetInput(street: String) {
        _uiState.update { it.copy(street = street) }
    }

    fun onConfirm() {
        Log.d("AddAddressViewModel", "AddAddressClicked")
        if (!validateAddressInputs()) return
        _uiState.update { it.copy(isLoading = true, errorMessage = "") }
        viewModelScope.launch {
            val currentUser = _user.value
            if (currentUser == null) {
                _uiState.update { it.copy(errorMessage = "Không thể xác định người dùng") }
                return@launch
            }
            val userName = currentUser.lastName + " " + currentUser.firstName
            val userLocationModel = UserLocationModel(
                id = "",
                userName = userName,
                street = _uiState.value.street,
                province = _uiState.value.selectedProvince!!.name,
                district = _uiState.value.selectedDistrict!!.name,
                ward = _uiState.value.selectedWard!!.name,
                userId = currentUser.id,
                provinceId = _uiState.value.selectedProvince!!.codename,
                districtId = _uiState.value.selectedDistrict!!.codename,
                wardId = _uiState.value.selectedWard!!.codename,
            )

            val result = repository.addAddressToFireStore(userLocationModel)
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Thêm d/c thành công!",
                    )
                }

            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message ?: "Có lỗi xảy ra"
                    )
                }
            }
        }

    }

    private fun validateAddressInputs(): Boolean {
        return when {
            _uiState.value.selectedProvince == null -> {
                _uiState.update { it.copy(errorMessage = "Vui lòng chọn Tỉnh/Thành phố") }
                Log.e("AddAddressViewModel", "Province is null")
                false
            }

            _uiState.value.selectedDistrict == null -> {
                _uiState.update { it.copy(errorMessage = "Vui lòng chọn Quận/Huyện") }
                Log.e("AddAddressViewModel", "District is null")
                false
            }

            _uiState.value.selectedWard == null -> {
                _uiState.update { it.copy(errorMessage = "Vui lòng chọn Phường/Xã") }
                Log.e("AddAddressViewModel", "Ward is null")
                false
            }

            else -> true
        }
    }
}

