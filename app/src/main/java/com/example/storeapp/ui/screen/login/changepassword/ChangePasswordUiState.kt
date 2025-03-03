package com.example.storeapp.ui.screen.login.changepassword


data class ChangePasswordUiState(
//    val email: String = "",
    val oldPassword: String = "",
    val newPassword: String = "",
    val confirmNewPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)