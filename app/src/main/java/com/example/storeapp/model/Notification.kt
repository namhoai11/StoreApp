package com.example.storeapp.model




data class Notification(

    val id: Int = 0,

    val notificationType: String,

    val firstProductName: String = "",

    val quantityCheckout: Int = 0,

    val firstProductImage: String = "",

    val message: String,

    val messageDetail: String = "",

    val date: String = getCurrentFormattedDate(),

    val isRead: Boolean = false
)
