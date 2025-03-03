package com.example.storeapp.ui.screen.profile.editprofile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import com.example.storeapp.model.Gender
import com.example.storeapp.model.UserModel
import com.example.storeapp.ui.component.function.toTimestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileEditViewModel(
    private val repository: FirebaseFireStoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileEditUiState())
    val uiState: StateFlow<ProfileEditUiState> = _uiState

    private val _user = MutableLiveData<UserModel?>()
    val user: LiveData<UserModel?> = _user

    init {
        loadUser()

        viewModelScope.launch {
            _uiState.collect { state ->
                Log.d("ProfileEditViewModel", "Current UI State: $state")
            }
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val userData = repository.getCurrentUser()
                if (userData != null) {
                    _user.postValue(userData)
                    Log.d("ProfileEditViewModel", "User loaded: $userData")

                    loadData(userData)
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Không tìm thấy người dùng"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("ProfileEditViewModel", "Error loading user", e)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Lỗi khi tải người dùng"
                    )
                }
            }
        }
    }

    private fun loadData(userData: UserModel) {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        firstName = userData.firstName,
                        lastName = userData.lastName,
                        dateOfBirth = userData.dateOfBirth,
                        gender = userData.gender,
                        phone = userData.phone,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                Log.e("ProfileEditViewModel", "Unexpected error", e)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Lỗi không xác định: ${e.message}"
                    )
                }
            }
        }
    }

    fun onFirstNameChange(newValue: String) = viewModelScope.launch {
        try {
            _uiState.update {
                it.copy(
                    firstName = newValue
                )
            }
            Log.d("SignUpViewModel", "FirstName: ${_uiState.value.firstName}")

        } catch (_: Exception) {

        }
    }

    fun onLastNameChange(newValue: String) = viewModelScope.launch {
        try {
            _uiState.update {
                it.copy(
                    lastName = newValue
                )
            }
            Log.d("SignUpViewModel", "LastName: ${_uiState.value.lastName}")


        } catch (_: Exception) {

        }
    }

    fun onDateOfBirthChange(newDate: String) {
        val timestamp = newDate.toTimestamp() // Chuyển chuỗi thành Timestamp
        _uiState.update { it.copy(dateOfBirth = timestamp) }
    }

    fun onGenderSelected(gender: Gender) {
        _uiState.update {
            it.copy(
                gender = gender
            )
        }
    }

    fun onPhoneChange(newValue: String) = viewModelScope.launch {
        try {
            _uiState.update {
                it.copy(
                    phone = newValue
                )
            }

        } catch (_: Exception) {

        }
    }


    fun updateUserInfo(navigateUp:()->Unit) {
        viewModelScope.launch {
            if (!validateInputs()) return@launch
            _uiState.update { it.copy(isLoading = true) }

            val currentUser = _user.value ?: return@launch
            val updatedUser = currentUser.copy( // Cập nhật từ UI State
                firstName = _uiState.value.firstName,
                lastName = _uiState.value.lastName,
                dateOfBirth = _uiState.value.dateOfBirth,
                gender = _uiState.value.gender,
                phone = _uiState.value.phone
            )

            val result = repository.updateUser(updatedUser)
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, successMessage = "Cập nhật thành công!") }
                navigateUp()
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = "Lỗi: ${error.message}") }
            }
        }
    }


    private fun validateInputs(): Boolean {
        return when {
            _uiState.value.firstName.isBlank() -> {
                _uiState.update { it.copy(errorMessage = "Vui lòng nhập Họ") }
                false
            }

            _uiState.value.lastName.isBlank() -> {
                _uiState.update { it.copy(errorMessage = "Vui lòng nhập Tên") }
                false
            }


            _uiState.value.phone.isBlank() -> {
                _uiState.update { it.copy(errorMessage = "Vui lòng nhập Số điện thoại") }
                false
            }



            else -> true
        }
    }

}
