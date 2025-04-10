package com.example.storeapp.ui.screen.home

import com.example.storeapp.model.CategoryModel
import com.example.storeapp.model.ProductModel
import com.example.storeapp.model.SliderModel

data class HomeUiState(
    val banners: List<SliderModel> = emptyList(),
    val categories: List<CategoryModel> = emptyList(),
    val allProducts: List<ProductModel> = emptyList(),
    val recommendedProducts: List<ProductModel> = emptyList(),
    val itemsByCategory: Map<String, List<ProductModel>> = emptyMap(),
    val currentCategoryId: String = "-2",
    val showBannerLoading: Boolean = true,
    val showCategoryLoading: Boolean = true,
    val showRecommendedLoading: Boolean = true,
    val cartQuantity: Int = 0,
    val isUserLoading: Boolean = true,
) {
    val currentListItems: List<ProductModel>
        get() = when (currentCategoryId) {
            "-2" -> allProducts // Hiển thị tất cả mục
            "-1" -> recommendedProducts
            else -> itemsByCategory[currentCategoryId] ?: emptyList()
        }
}