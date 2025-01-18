package com.example.storeapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Entity
data class Checkout(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "receiver_name")
    val receiverName: String,

    @ColumnInfo(name = "receiver_address")
    val receiverAddress: String,

    @ColumnInfo(name = "order_items")
    val orderItems: List<Cart>,

    @ColumnInfo(name = "shipping_method")
    val shippingMethod: String,

    @ColumnInfo(name = "coupon")
    val coupon: String,

    @ColumnInfo(name = "formatted_order_date")
    val formattedCheckoutDate: String = getCurrentFormattedDate(),

    @ColumnInfo(name = "formatted_order_time")
    val formattedCheckoutTime: String = getCurrentFormattedTime(),

    @ColumnInfo(name = "checkout_date")
    val checkoutDate: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "shipping_cost")
    val shippingCost: Double,

    @ColumnInfo(name = "shipping_description")
    val shippingDescription: String,

    @ColumnInfo(name = "payment_method")
    val paymentMethod: String = "",

    @ColumnInfo(name = "total_price")
    val totalPrice: Double,
)

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