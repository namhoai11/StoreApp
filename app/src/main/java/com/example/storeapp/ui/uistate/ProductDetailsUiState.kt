package com.example.storeapp.ui.uistate

import androidx.core.app.NotificationCompat.MessagingStyle.Message
import com.example.storeapp.model.ItemsModel

fun defaultItemsModel() = ItemsModel(
    id = 0,
    title = "",
    description = "",
    picUrl = ArrayList(),
    model = ArrayList(),
    price = 0.0,
    rating = 0.0,
    showRecommended = false,
    categoryId = ""
)
data class ProductDetailsUiState(
    val productDetailsItem: ItemsModel = defaultItemsModel(),
    val showProductDetailsLoading: Boolean = true,
    val errorMessage: String = ""
)
