package com.example.storeapp.model


import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class CheckoutModel(
    val id: Int = 0,
    val receiverName: String,
    val receiverAddress: String,
    val orderItems: List<CartModel>,
    val shippingMethod: String,
    val coupon: String,
//    val formattedCheckoutDate: String = getCurrentFormattedDate(),
//    val formattedCheckoutTime: String = getCurrentFormattedTime(),
    val checkoutDate: Long = System.currentTimeMillis(),
    val shippingCost: Double,
    val shippingDescription: String,
    val paymentMethod: String = "",
    val totalPrice: Double,
){
    init {
        require(receiverName.isNotBlank()) { "Receiver name cannot be blank." }
        require(receiverAddress.isNotBlank()) { "Receiver address cannot be blank." }
        require(orderItems.isNotEmpty()) { "Order items cannot be empty." }
        require(totalPrice > 0) { "Total price must be greater than 0." }
    }
}

fun getCurrentFormattedDate(): String {
    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
    return dateFormat.format(Date())
}

fun getCurrentFormattedTime(): String {
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    timeFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
    return timeFormat.format(Date())
}