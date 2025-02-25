package com.example.storeapp.ui.screen.admin.manage.category.add_category

import android.net.Uri
import com.example.storeapp.model.CategoryModel

data class AddCategoryUiState(
    val categoryDetailsItem: CategoryModel = CategoryModel(),
    val currentImageSelected: Uri? = null,
    val isEditing: Boolean = true,
    val isLoading: Boolean = false,  // Xử lý trạng thái tải API
    val errorMessage: String? = null, // Xử lý lỗi nếu có
    val successMessage: String = "",
)