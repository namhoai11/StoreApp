package com.example.storeapp.ui.screen.admin.manage.user.userdetails

import com.example.storeapp.model.UserModel


data class UserDetailsManagementUiState(
    val user: UserModel = UserModel(),

    val userSpend: Double = 0.0,

    val isShowSetRoleDialog: Boolean = false,

    val currentButtonText: String = "",

    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)