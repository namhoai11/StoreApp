package com.example.storeapp.ui.screen.address

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import com.example.storeapp.model.UserLocationModel
import com.example.storeapp.model.UserModel
import com.example.storeapp.ui.screen.productdetails.ProductDetailsDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddressViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: FirebaseFireStoreRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddressUiState())
    val uiState: StateFlow<AddressUiState> = _uiState

    private val _user = MutableLiveData<UserModel?>()
    val user: LiveData<UserModel?> = _user

    private val addressSetupRole: Int =
        checkNotNull(savedStateHandle[AddressDestination.addressSetupRole])


    init {
        loadUser()
        checkIsDefaultLocationSetting()
        viewModelScope.launch {
            _uiState.collect { state ->
                Log.d("AddressViewModel", "Current UI State: $state")
            }
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            val userData = repository.getCurrentUser()
            _user.value = userData
            Log.d("AddressViewModel", "User loaded: $userData")
            loadAddress()
        }
    }

    private fun checkIsDefaultLocationSetting() {
        if (addressSetupRole == 0) {
            _uiState.update { it.copy(isDefaultLocationSetting = true) }
        } else {
            _uiState.update { it.copy(isDefaultLocationSetting = false) }
        }
    }

    fun loadAddress() = viewModelScope.launch {
        try {
            Log.d("AddressViewModel", "Start Loaded addresses")

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    successMessage = null
                )
            }
            val currentUser = _user.value
            if (currentUser == null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Không thể xác định người dùng"
                    )
                }
                return@launch
            }

            val result = repository.getListAddressByUser(currentUser.id)
            if (result.isSuccess) {
                val listAddress = result.getOrDefault(emptyList())

                val defaultLocationId = currentUser.defaultLocationId
                val validDefaultLocationId = if (listAddress.any { it.id == defaultLocationId }) {
                    defaultLocationId
                } else {
                    null // Nếu không có trong danh sách thì để null
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        addressList = listAddress,
                        selectedItemId = validDefaultLocationId,
                        successMessage = "Tải danh sách địa chỉ thành công"
                    )
                }

                Log.d("AddressViewModel", "Loaded ${listAddress.size} addresses")
            } else {
                throw result.exceptionOrNull()
                    ?: Exception("Lỗi không xác định khi tải danh sách địa chỉ")
            }
        } catch (e: Exception) {
            Log.e("AddressViewModel", "Error loading Address: ${e.message}")
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "Lỗi tải danh sách địa chỉ: ${e.message}"
                )
            }
        }
    }

    fun onAddressItemSelected(addressId: String) {
        _uiState.update {
            it.copy(
                selectedItemId = addressId
            )
        }
    }

    fun onSetDefaultLocationClicked() = viewModelScope.launch {
        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )
        }
        val currentUser = _user.value
        if (currentUser == null) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "Không thể xác định người dùng"
                )
            }
            return@launch
        }

        val selectedItemId = uiState.value.selectedItemId
        if (selectedItemId.isNullOrEmpty()) {
            _uiState.update { it.copy(errorMessage = "Vui lòng chọn địa chỉ mặc định") }
            return@launch
        }
        val result = repository.updateDefaultLocation(
            userId = currentUser.id,
            newDefaultLocationId = selectedItemId
        )
        if (result.isSuccess) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    successMessage = "Them D/c mac dinh thanh cong!",
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

    fun confirmDeleteLocationClicked(addressId: String) = viewModelScope.launch {
        val currentUser = _user.value
        if (currentUser == null) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "Không thể xác định người dùng"
                )
            }
            return@launch
        }
        var oldList = emptyList<UserLocationModel>()
        _uiState.update { currentState ->
            oldList = currentState.addressList

            val updateList = currentState.addressList.filterNot { it.id == addressId }
            val selectedItemId =
                if (addressId == currentUser.defaultLocationId) null else currentState.selectedItemId
            currentState.copy(
                addressList = updateList,
                selectedItemId = selectedItemId
            )
        }
        val result=repository.deleteAddressById(addressId)
        // Kiểm tra kết quả trả về từ API
        if (result.isFailure) {
            // Nếu có lỗi, thực hiện rollback và hiển thị thông báo lỗi
            _uiState.update {
                it.copy(
                    addressList = oldList,
                    errorMessage = "Lỗi khi xoá dia chi: ${result.exceptionOrNull()?.message}"
                )
            }
            Log.e("CartViewModel", "Lỗi khi xoá sản phẩm: ${result.exceptionOrNull()?.message}")
        } else {
            // Nếu thành công, cập nhật trạng thái UI
            _uiState.update { it.copy(successMessage = "Đã xoá d/c") }
        }
    }

}