package com.example.storeapp.ui.uistate

import com.example.storeapp.model.ItemsModel

data class OurProductUiState (
    val allItems: List<ItemsModel> = emptyList(),
    val showItemsLoading: Boolean = true,
)