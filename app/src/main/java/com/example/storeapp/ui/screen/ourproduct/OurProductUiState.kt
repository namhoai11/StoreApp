package com.example.storeapp.ui.screen.ourproduct

import com.example.storeapp.model.ProductModel

data class OurProductUiState(
    val allProducts: List<ProductModel> = emptyList(),
    val productsSearched: List<ProductModel> = emptyList(),
    val currentQuery: String = "",
    val showProductsLoading: Boolean = true,
    val showProductsSearchedLoading: Boolean = false // Set mặc định là false
)
