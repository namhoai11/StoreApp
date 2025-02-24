package com.example.storeapp.ui.screen.admin.manage.product

import com.example.storeapp.model.CategoryModel
import com.example.storeapp.model.ProductModel

data class ProductManagementUiState(
    val categories: List<CategoryModel> = emptyList(),
    val allProducts: List<ProductModel> = emptyList(),
    val itemsByCategory: Map<String, List<ProductModel>> = emptyMap(),
    val currentCategoryId: String = "-2",
    val showCategoryLoading: Boolean = true,
    val showRecommendedLoading: Boolean = true,
) {
    val currentListItems: List<ProductModel>
        get() = when (currentCategoryId) {
           "-2" -> allProducts // Hiển thị tất cả mục
            else -> itemsByCategory[currentCategoryId] ?: emptyList()
        }
}