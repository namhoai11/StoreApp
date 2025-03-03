package com.example.storeapp.ui.screen.admin.manage.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import com.example.storeapp.model.Role
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserManagementViewModel(private val repository: FirebaseFireStoreRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(UserManagementUiState())
    val uiState: StateFlow<UserManagementUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }

        val allUsers = repository.getAllUser()

        allUsers.onSuccess { users ->
            Log.d("UserManagementViewModel", "All User:$users")
            val userByRole = users.groupBy { it.role }
            _uiState.update {
                it.copy(
                    listUser = users,
                    listUserByRole = userByRole,
                    isLoading = false
                )
            }
        }.onFailure { e ->
            Log.e("UserManagementViewModel", "Lỗi khi tải users", e)
            _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
        }


    }

    fun selectRole(role: String) {
        _uiState.update { it.copy(currentUserRole = Role.fromString(role)!!) }
    }

    fun searchUsersByName(query: String) = viewModelScope.launch {
        _uiState.update { currentState ->
            val filteredUsers = if (query.isNotBlank()) {
                currentState.listUser.filter {
                    val fullName = "${it.firstName} ${it.lastName}"
                    fullName.contains(query, ignoreCase = true) // So sánh không phân biệt hoa thường
                }
            } else {
                currentState.currentListUser
            }
            currentState.copy(
                usersSearched = filteredUsers,
                currentQuery = query
            )
        }
        Log.d("UserManagementViewModel", "usersSearched:${_uiState.value.usersSearched}")
    }

}