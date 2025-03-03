package com.example.storeapp.ui.screen.login.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.FirebaseAuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val auth: FirebaseAuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState


    fun sendPasswordResetEmail(navigateSignIn: () -> Unit) {
        val email = _uiState.value.email
        if (email.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Vui lòng nhập email") }
            return
        }
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = auth.sendPasswordResetEmail(email)
            result.onSuccess {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Email đặt lại mật khẩu đã được gửi!"
                    )
                }
                navigateSignIn()
            }.onFailure { e ->
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message)
                }
            }
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

}