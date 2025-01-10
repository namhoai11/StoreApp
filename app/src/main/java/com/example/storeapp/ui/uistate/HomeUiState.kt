package com.example.storeapp.ui.uistate

import com.example.storeapp.model.CategoryModel
import com.example.storeapp.model.ItemsModel
import com.example.storeapp.model.SliderModel

data class HomeUiState(
    val banners: List<SliderModel> = emptyList(),
    val categories: List<CategoryModel> = emptyList(),
    val allItems: List<ItemsModel> = emptyList(),
    val recommendedItems: List<ItemsModel> = emptyList(),
    val itemsByCategory: Map<Int, List<ItemsModel>> = emptyMap(),
    val currentCategoryId: Int? = -2,
    val showBannerLoading: Boolean = true,
    val showCategoryLoading: Boolean = true,
    val showRecommenedLoading: Boolean = true
) {
    val currentListItems: List<ItemsModel>
        get() = when (currentCategoryId) {
            -2 -> allItems // Hiển thị tất cả mục
            -1 -> recommendedItems
            else -> itemsByCategory[currentCategoryId] ?: emptyList()
        }
}