package com.example.storeapp.model

import androidx.compose.ui.graphics.Color

//data class Coupon(
//    val discountedPrice: Double,       // Giá trị giảm giá
//    val description: String,          // Mô tả coupon
//    val expiredDate: LocalDate,       // Ngày hết hạn
//    val couponCode: String,           // Mã giảm giá
//    val maxDiscount: Double? = null,  // Giới hạn giảm giá tối đa (nếu có)
//    val minOrderValue: Double? = null,// Giá trị đơn hàng tối thiểu để áp dụng (nếu có)
////    val timesUsed: Int = 0,           // Số lần đã sử dụng
//    val isActive: Boolean = true,     // Trạng thái hoạt động của coupon
//    val color1: Color,                // Màu gradient 1
//    val color2: Color                 // Màu gradient 2
//)

data class CouponModel(
    val discountedPrice: String,       // Giá trị giảm giá
    val description: String,          // Mô tả coupon
    val expiredDate: String,       // Ngày hết hạn
    val couponCode: String,           // Mã giảm giá
    val maxDiscount: Double? = null,  // Giới hạn giảm giá tối đa (nếu có)
    val minOrderValue: Double? = null,// Giá trị đơn hàng tối thiểu để áp dụng (nếu có)
//    val timesUsed: Int = 0,           // Số lần đã sử dụng
    val isActive: Boolean = true,     // Trạng thái hoạt động của coupon
    val color1: Color,                // Màu gradient 1
    val color2: Color                 // Màu gradient 2
)