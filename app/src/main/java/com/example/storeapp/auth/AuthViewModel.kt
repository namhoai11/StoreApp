package com.example.storeapp.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.FirebaseAuthRepository
import com.example.storeapp.model.UserModel
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val firebaseAuthRepository = FirebaseAuthRepository()

    fun register(email: String, password: String, userModel: UserModel) {
        viewModelScope.launch {
            val result = firebaseAuthRepository.registerUser(email, password, userModel)
            // Xử lý kết quả
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = firebaseAuthRepository.loginUser(email, password)
            // Xử lý kết quả
        }
    }

    fun logout() {
        firebaseAuthRepository.logoutUser()
    }

    fun getCurrentUser() = firebaseAuthRepository.getCurrentUser()
}
