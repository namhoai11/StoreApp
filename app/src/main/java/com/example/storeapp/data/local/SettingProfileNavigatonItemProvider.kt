package com.example.storeapp.data.local

import androidx.annotation.DrawableRes
import com.example.storeapp.R
import com.example.storeapp.ui.screen.admin.dashboard.DashboardAdminDestination

object SettingProfileNavigatonItemProvider {
    val navigationItemList = listOf(
        SettingProfileNavigaton(
            title = "Địa chỉ",
            description = "Cập nhật địa chỉ nhận hàng",
            icon = R.drawable.icon_pinlocation
        ),
        SettingProfileNavigaton(
            title = "Giỏ hàng",
            description = "Ghé thăm giỏ hàng hiện tại",
            icon = R.drawable.icon_cart_outlined
        ),
        SettingProfileNavigaton(
            title = "Đơn hàng",
            description = "Thông tin đơn hàng đã thực hiện",
            icon = R.drawable.icon_order
        ),
        SettingProfileNavigaton(
            title = "Ngân hàng",
            description = "Thông tin tài khoản ngân hàng liên kết",
            icon = R.drawable.credit_card
        ),
        SettingProfileNavigaton(
            title = "Phiếu giảm giá",
            description = "ShopVoucher dành riêng cho bạn",
            icon = R.drawable.icon_coupon_outlined
        ),
        SettingProfileNavigaton(
            title = "Thông báo",
            description = "Thông báo mới nhất",
            icon = R.drawable.icon_notification_outlined
        ),
        SettingProfileNavigaton(
            title = "Bảo mật tài khoản",
            description = "Các chính sách và chế độ bảo mật",
            icon = R.drawable.protection
        ),
        SettingProfileNavigaton(
            title = "Admin",
            description = "Trang quản trị",
            icon = R.drawable.administrator,
            route = DashboardAdminDestination.route,
            requiredRole = 1,
        ),
    )
}


data class SettingProfileNavigaton(
    val title: String,
    val description: String,
    @DrawableRes val icon: Int,
    val route: String = "",
    val requiredRole: Int = 0 // Mặc định ai cũng thấy (0: User, 1: Admin)
)

