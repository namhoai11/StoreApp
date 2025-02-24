package com.example.storeapp.ui.screen.admin.manage.category

import com.example.storeapp.model.CategoryModel
import com.example.storeapp.model.ProductModel

data class CategoryManagementUiState(
    val categories: List<CategoryModel> = emptyList(),
    val allProducts: List<ProductModel> = emptyList(),
    val categoriesSearched: List<CategoryModel> = emptyList(),
    val currentQuery: String = "",

    val showCategoryLoading: Boolean = true,

)