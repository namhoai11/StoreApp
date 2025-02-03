package com.example.storeapp.ui.uistate

import com.example.storeapp.model.CategoryModel
import com.example.storeapp.model.ProductModel
import com.example.storeapp.model.SliderModel

data class HomeUiState(
    val banners: List<SliderModel> = emptyList(),
    val categories: List<CategoryModel> = emptyList(),
    val allProducts: List<ProductModel> = emptyList(),
    val recommendedProducts: List<ProductModel> = emptyList(),
    val itemsByCategory: Map<Int, List<ProductModel>> = emptyMap(),
    val currentCategoryId: Int? = -2,
    val showBannerLoading: Boolean = true,
    val showCategoryLoading: Boolean = true,
    val showRecommendedLoading: Boolean = true
) {
    val currentListItems: List<ProductModel>
        get() = when (currentCategoryId) {
            -2 -> allProducts // Hiển thị tất cả mục
            -1 -> recommendedProducts
            else -> itemsByCategory[currentCategoryId] ?: emptyList()
        }
}