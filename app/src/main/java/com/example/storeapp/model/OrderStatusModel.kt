//package com.example.storeapp.model
//
//sealed class OrderStatusModel(val title: String) {
//    data object All : OrderStatusModel("Tất cả")
//    data object Pending : OrderStatusModel("Chờ Xác nhận")
//    data object Confirmed : OrderStatusModel("Đã Xác nhận")
//    data object Shipping : OrderStatusModel("Đang giao")
//    data object Completed : OrderStatusModel("Hoàn Thành")
//    data object Canceled : OrderStatusModel("Đã Hủy")
//
//    companion object {
//        fun fromTitle(title: String): OrderStatusModel = when (title) {
//            "Tất cả" -> All
//            "Chờ Xác nhận" -> Pending
//            "Đã Xác nhận" -> Confirmed
//            "Đang giao" -> Shipping
//            "Hoàn Thành" -> Completed
//            "Đã Hủy" -> Canceled
//            else -> throw IllegalArgumentException("Trạng thái không hợp lệ: $title")
//        }
//    }
//}
