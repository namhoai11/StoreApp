package com.example.storeapp.ui.uistate

import com.example.storeapp.model.ProductModel

data class OurProductUiState(
    val allItems: List<ProductModel> = emptyList(),
    val itemsSearched: List<ProductModel> = emptyList(),
    val currentQuery: String = "",
    val showItemsLoading: Boolean = true,
    val showItemsSearchedLoading: Boolean = false // Set mặc định là false
)
