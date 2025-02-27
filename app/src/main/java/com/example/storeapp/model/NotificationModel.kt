package com.example.storeapp.model




data class NotificationModel(
    val id: Int = 0,
    val notificationType: String,
    val firstProductName: String = "",
    val quantityCheckout: Int = 0,
    val firstProductImage: String = "",
    val message: String,
    val messageDetail: String = "",
    val date: String = "",
    val isRead: Boolean = false
)
