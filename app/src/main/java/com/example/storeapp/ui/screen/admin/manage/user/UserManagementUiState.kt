package com.example.storeapp.ui.screen.admin.manage.user

import com.example.storeapp.model.Role
import com.example.storeapp.model.UserModel

data class UserManagementUiState(
    val listUser: List<UserModel> = emptyList(),
    val currentUserRole: Role = Role.USER,
    val listUserByRole: Map<Role, List<UserModel>> = emptyMap(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,

    val usersSearched: List<UserModel> = emptyList(),
    val currentQuery: String = "",
) {
    val currentListUser: List<UserModel>
        get() = listUserByRole[currentUserRole] ?: emptyList()

}