package com.example.storeapp.ui.screen.profile.profiledetails


data class ProfileDetailUiState(
//    val currentUser: UserModel = UserModel(),
    val userSpend: Double = 0.0,
    val isEditing: Boolean = true,
    val isLoading: Boolean = false,  // Xử lý trạng thái tải API
    val errorMessage: String? = null, // Xử lý lỗi nếu có
    val successMessage: String = "",
)