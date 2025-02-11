package com.example.storeapp.ui.screen.favorite

import com.example.storeapp.model.WishListModel

data class WishListUiState(
    val listItems : List<WishListModel> = emptyList(),
    val showWishListLoading: Boolean = true,
    val errorMessage: String = ""
)
