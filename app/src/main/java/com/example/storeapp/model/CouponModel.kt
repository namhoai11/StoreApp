package com.example.storeapp.model

import androidx.compose.ui.graphics.Color
import com.google.firebase.Timestamp

data class CouponModel(
    val id: String,                     // ID coupon
    val code: String,                    // Mã giảm giá
    val name: String,                    // Tên coupon
    val description: String = "",        // Mô tả coupon
    val type: String,                     // Loại giảm giá (PERCENTAGE / FIXED_AMOUNT)
    val value: Double,                    // Giá trị giảm (ví dụ: 10% hoặc 50K)
    val maxDiscount: Double? = null,      // Mức giảm tối đa (nếu có)
    val minOrderValue: Double? = null,    // Giá trị đơn hàng tối thiểu để áp dụng
    val quantity: Int = 1,                // Số lượng coupon khả dụng
    val isActive: Boolean = true,         // Coupon còn hoạt động không?
    val startDate: Timestamp,             // Ngày bắt đầu
    val endDate: Timestamp,               // Ngày hết hạn
    val color1: Color? = null,            // Màu UI (có thể null nếu không cần)
    val color2: Color? = null             // Màu UI (có thể null nếu không cần)
)

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
//
//data class CouponModel(
//    val discountedPrice: String,       // Giá trị giảm giá
//    val description: String,          // Mô tả coupon
//    val expiredDate: String,       // Ngày hết hạn
//    val couponCode: String,           // Mã giảm giá
//    val maxDiscount: Double? = null,  // Giới hạn giảm giá tối đa (nếu có)
//    val minOrderValue: Double? = null,// Giá trị đơn hàng tối thiểu để áp dụng (nếu có)
////    val timesUsed: Int = 0,           // Số lần đã sử dụng
//    val isActive: Boolean = true,     // Trạng thái hoạt động của coupon
//    val color1: Color,                // Màu gradient 1
//    val color2: Color                 // Màu gradient 2
//)