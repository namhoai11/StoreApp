package com.example.storeapp.ui.component.function

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun timestampToDateString(timestamp: Timestamp): String {
    val date = Date(timestamp.seconds * 1000) // Chuyển đổi giây thành mili giây
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()) // Định dạng ngày
    return sdf.format(date)
}

fun timestampToDateOnlyString(timestamp: Timestamp): String {
    val date = Date(timestamp.seconds * 1000) // Chuyển đổi giây thành mili giây
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Chỉ lấy ngày tháng
    return sdf.format(date)
}