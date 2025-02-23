package com.example.storeapp.ui.component.function

import java.text.DecimalFormat

fun formatCurrency2(amount: Double): String {
    val formatter = DecimalFormat("#,###")
    return formatter.format(amount) + " ₫"
}

fun formatNumber(amount: Double): String {
    val formatter = DecimalFormat("#")
    return formatter.format(amount)
}
