package com.example.storeapp.ui.screen.login.changepassword

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.FirebaseAuthRepository
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import com.example.storeapp.model.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    private val repository: FirebaseFireStoreRepository,
    private val auth: FirebaseAuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState

    private val _user = MutableLiveData<UserModel?>()
    val user: LiveData<UserModel?> = _user

    init {
        loadUser()

        viewModelScope.launch {
            _uiState.collect { state ->
                Log.d("ChangePasswordViewModel", "Current UI State: $state")
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
                    Log.d("ChangePasswordViewModel", "User loaded: $userData")
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Không tìm thấy người dùng"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("ChangePasswordViewModel", "Error loading user", e)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Lỗi khi tải người dùng"
                    )
                }
            }
        }
    }

    fun onOldPasswordChange(newValue: String) = viewModelScope.launch {
        try {
            _uiState.update {
                it.copy(
                    oldPassword = newValue
                )
            }

        } catch (_: Exception) {

        }
    }

    fun onNewPasswordChange(newValue: String) = viewModelScope.launch {
        try {
            _uiState.update {
                it.copy(
                    newPassword = newValue
                )
            }

        } catch (_: Exception) {

        }
    }


    fun onConfirmNewPasswordChange(newValue: String) = viewModelScope.launch {
        try {
            _uiState.update {
                it.copy(
                    confirmNewPassword = newValue
                )
            }

        } catch (_: Exception) {

        }
    }


    fun changePassword(navigate: () -> Unit) {
        val state = _uiState.value

        if (state.oldPassword.isBlank() || state.newPassword.isBlank() || state.confirmNewPassword.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Vui lòng nhập đầy đủ thông tin") }
            Log.d("ChangePassWordViewModel", "${_uiState.value.errorMessage}")
            return
        }

        if (state.newPassword != state.confirmNewPassword) {
            _uiState.update { it.copy(errorMessage = "Mật khẩu mới không khớp") }
            Log.e("ChangePassWordViewModel", "${_uiState.value.errorMessage}")
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = auth.updateUserPassword(state.oldPassword, state.newPassword)

            result.onSuccess {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Cập nhật mật khẩu thành công!"
                    )
                }
                navigate()
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                Log.e("ChangePassWordViewModel", "${_uiState.value.errorMessage}")

            }
        }
    }

}