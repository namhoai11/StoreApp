package com.example.storeapp.ui.uistate

import com.example.storeapp.model.ItemsModel

data class OurProductUiState(
    val allItems: List<ItemsModel> = emptyList(),
    val itemsSearched: List<ItemsModel> = emptyList(),
    val currentQuery: String = "",
    val showItemsLoading: Boolean = true,
    val showItemsSearchedLoading: Boolean = false // Set mặc định là false
)
