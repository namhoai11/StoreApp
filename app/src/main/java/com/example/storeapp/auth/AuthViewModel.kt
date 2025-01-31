package com.example.storeapp.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.AuthRepository
import com.example.storeapp.data.repository.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    init {
        checkUserLoginStatus()
    }

    private fun checkUserLoginStatus() = viewModelScope.launch {
        val currentUser = authRepository.getCurrentUser()
        _user.value = currentUser
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        try {
            val user = authRepository.login(email, password)
            _user.value = user
        } catch (e: Exception) {
            // Xử lý lỗi đăng nhập
            Log.e("AuthViewModel", "Login failed", e)
        }
    }

    fun logout() = viewModelScope.launch {
        authRepository.logout()
        _user.value = null
    }
}
