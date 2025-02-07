package com.example.storeapp.ui.screen.login


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.FirebaseAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

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

    fun SignIn(
        onNavigateHome: () -> Unit,
    ) {
        if (!validateInputs()) return

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    firebaseAuthRepository.loginUser(_uiState.value.email, _uiState.value.password)
                }
                result.onSuccess { authResult ->
                    val user = authResult.user
                    if (user != null) {
                        if (user.isEmailVerified) {
                            Log.d("LoginViewModel", "Đăng nhập thành công: ${user.uid}")
                            _uiState.update { currentState ->
                                currentState.copy(
                                    isLoading = false,
                                    successMessage = "Đăng nhập thành công!"
                                )
                            }
                            onNavigateHome()

                        } else {
                            Log.w("LoginViewModel", "Tài khoản chưa được xác thực email.")
                            user.sendEmailVerification()
                            _uiState.update { currentState ->
                                currentState.copy(
                                    isLoading = false,
                                    errorMessage = "Tài khoản chưa được xác thực. Vui lòng kiểm tra email của bạn."
                                )
                            }
                        }
                    } else {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                errorMessage = "Không thể lấy thông tin người dùng."
                            )
                        }
                    }
                }.onFailure { e ->
                    Log.e("LoginViewModel", "Lỗi đăng nhập: ${e.message}", e)
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            errorMessage = e.localizedMessage ?: "Đăng nhập thất bại!"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Lỗi ngoại lệ khi đăng nhập: ${e.message}", e)
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        errorMessage = "Lỗi không xác định khi đăng nhập!"
                    )
                }
            }
        }
    }


    fun clearSuccessMessage() {
        _uiState.value = _uiState.value.copy(successMessage = null)
    }

    private fun validateInputs(): Boolean {
        return when {
            _uiState.value.email.isBlank() -> {
                _uiState.update { it.copy(errorMessage = "Vui lòng nhập Email") }
                false
            }

            _uiState.value.password.isBlank() -> {
                _uiState.update { it.copy(errorMessage = "Vui lòng nhập Mật khẩu") }
                false
            }

            else -> true
        }
    }
}