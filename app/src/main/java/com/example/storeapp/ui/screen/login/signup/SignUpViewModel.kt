package com.example.storeapp.ui.screen.login.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.FirebaseAuthRepository
import com.example.storeapp.model.UserModel
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class SignUpViewModel(
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState

    fun onFirstNameChange(newValue: String) = viewModelScope.launch {
        try {
            _uiState.update {
                it.copy(
                    firstName = newValue
                )
            }
            Log.d("SignUpViewModel","FirstName: ${_uiState.value.firstName}")

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
            Log.d("SignUpViewModel","LastName: ${_uiState.value.lastName}")


        } catch (_: Exception) {

        }
    }

    fun onEmailChange(newValue: String) = viewModelScope.launch {
        try {
            _uiState.update {
                it.copy(
                    email = newValue
                )
            }

        } catch (_: Exception) {

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

    fun onPasswordChange(newValue: String) = viewModelScope.launch {
        try {
            _uiState.update {
                it.copy(
                    password = newValue
                )
            }

        } catch (_: Exception) {

        }
    }

    fun onConfirmPasswordChange(newValue: String) = viewModelScope.launch {
        try {
            _uiState.update {
                it.copy(
                    confirmPassword = newValue
                )
            }

        } catch (_: Exception) {

        }
    }

    fun onCheckedChange(isChecked: Boolean) = viewModelScope.launch {
        try {
            _uiState.update {
                it.copy(
                    isChecked = isChecked
                )
            }

        } catch (_: Exception) {

        }
    }

    fun signUp() {
        if (!validateInputs()) return

        // Cập nhật trạng thái UI cho việc loading và lỗi
        _uiState.update {
            it.copy(isLoading = true, errorMessage = null)
        }

        viewModelScope.launch {
            val userModel = UserModel(
                firstName = _uiState.value.firstName,
                lastName = _uiState.value.lastName,
                email = _uiState.value.email,
                phone = _uiState.value.phone,
                createdAt = Timestamp(Date()),  // Chuyển đổi từ thời gian hiện tại
                updatedAt = Timestamp(Date())   // Chuyển đổi từ thời gian hiện tại
            )

            // Đăng ký người dùng qua repository
            val result = firebaseAuthRepository.registerUser(
                _uiState.value.email,
                _uiState.value.password,
                userModel
            )

            result.onSuccess {
                _uiState.update { it.copy(isLoading = false) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
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

            _uiState.value.email.isBlank() -> {
                _uiState.update { it.copy(errorMessage = "Vui lòng nhập Email") }
                false
            }

            _uiState.value.phone.isBlank() -> {
                _uiState.update { it.copy(errorMessage = "Vui lòng nhập Số điện thoại") }
                false
            }

            _uiState.value.password.isBlank() -> {
                _uiState.update { it.copy(errorMessage = "Vui lòng nhập Mật khẩu") }
                false
            }

            _uiState.value.confirmPassword != _uiState.value.password -> {
                _uiState.update { it.copy(errorMessage = "Mật khẩu xác nhận không khớp") }
                false
            }

            !_uiState.value.isChecked -> {
                _uiState.update { it.copy(errorMessage = "Bạn cần đồng ý với điều khoản") }
                false
            }

            else -> true
        }
    }
}