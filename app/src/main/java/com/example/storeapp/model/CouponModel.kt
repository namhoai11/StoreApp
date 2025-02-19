package com.example.storeapp.model

import androidx.compose.ui.graphics.Color
import com.google.firebase.Timestamp

data class CouponModel(
    val id: String = "",                     // ID coupon mặc định là chuỗi rỗng
    val code: String = "",                // Mã giảm giá có thể null
    val name: String = "",                // Tên coupon có thể null
    val description: String = "",            // Mô tả mặc định là chuỗi rỗng
    val type: CouponType = CouponType.PERCENTAGE, // Loại mặc định là giảm giá phần trăm
    val value: Double = 0.0,                 // Giá trị giảm mặc định là 0.0
    val maxDiscount: Double? = null,         // Mức giảm tối đa có thể null
    val minOrderValue: Double? = null,       // Giá trị đơn hàng tối thiểu có thể null
    val quantity: Int = 1,                   // Số lượng mặc định là 1
    val active: Boolean = true,              // Mặc định coupon còn hoạt động
    val startDate: Timestamp = Timestamp.now(), // Ngày bắt đầu mặc định là thời điểm hiện tại
    val endDate: Timestamp = Timestamp.now(),   // Ngày hết hạn mặc định là thời điểm hiện tại
    val color1: Color? = null,               // Màu UI có thể null
    val color2: Color? = null                // Màu UI có thể null
)



enum class CouponType {
    PERCENTAGE,
    FIXED_AMOUNT,
    FREE_SHIPPING;

    override fun toString(): String {
        return when (this) {
            PERCENTAGE -> "Giảm giá theo %"
            FIXED_AMOUNT -> "Giảm giá số tiền"
            FREE_SHIPPING -> "Miễn phí vận chuyển"
        }
    }
}


enum class CouponActive {
    ALL,
    ONGOING,  // Đang diễn ra
    UPCOMING, // Sắp diễn ra
    EXPIRED;  // Hết hạn

    override fun toString(): String {
        return when (this) {
            ALL -> "Tất cả"
            ONGOING -> "Đang diễn ra"
            UPCOMING -> "Sắp diễn ra"
            EXPIRED -> "Hết hạn"
        }
    }

    companion object {
        fun fromString(value: String): CouponActive? {
            return values().find { it.toString() == value }
        }
    }
}


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