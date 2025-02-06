package com.example.storeapp.ui.screen.login.signup

import android.content.Context
import android.util.Log
import android.widget.Toast
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

    fun signUp(context: Context) {  // Truyền context vào để hiển thị Toast
        if (!validateInputs()) return

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val userModel = UserModel(
                firstName = _uiState.value.firstName,
                lastName = _uiState.value.lastName,
                email = _uiState.value.email,
                phone = _uiState.value.phone,
                createdAt = Timestamp(Date()),
                updatedAt = Timestamp(Date())
            )

            val result = firebaseAuthRepository.registerUser(
                _uiState.value.email,
                _uiState.value.password,
                userModel
            )

            result.onSuccess { authResult ->
                val user = authResult.user
                user?.sendEmailVerification()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Đăng ký thành công! Vui lòng kiểm tra email để xác thực tài khoản.", Toast.LENGTH_LONG).show()
                        Log.d("SignUpViewModel","Đăng ký thành công! Vui lòng kiểm tra email để xác thực tài khoản.")
                        _uiState.update {
                            it.copy(isLoading = false, successMessage = "Đăng ký thành công! Vui lòng kiểm tra email để xác nhận tài khoản.")
                        }
                    } else {
                        Toast.makeText(context, "Không thể gửi email xác thực: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        Log.e("SignUpViewModel","Không thể gửi email xác thực: ${task.exception?.message}")
                    }
                }

            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
                Toast.makeText(context, "Đăng ký thất bại: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun clearSuccessMessage() {
        _uiState.value = _uiState.value.copy(successMessage = null)
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