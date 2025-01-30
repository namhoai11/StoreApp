package com.example.storeapp.data.local

import androidx.compose.ui.graphics.Color
import com.example.storeapp.R
import com.example.storeapp.model.CartModel
import com.example.storeapp.model.CouponModel
import com.example.storeapp.model.OrderModel
import com.example.storeapp.model.PaymentMethodModel
import com.example.storeapp.model.ShippingModel
import com.example.storeapp.model.UserLocationModel
import com.google.firebase.Timestamp

object DataDummy {
    val dummyUserLocation = listOf(
        UserLocationModel(
            id = 1,
            name = "Mas Azi",
            address = "Jl. Durian No. 123, Banyubiru " +
                    "Kab. Semarang, Jawa Tengah " +
                    "Indonesia 50664"
        ),
        UserLocationModel(
            id = 2,
            name = "Haryanto",
            address = "Jl. Durian No. 123, Banyubiru " +
                    "Kab. Semarang, Jawa Tengah " +
                    "Indonesia 50664"
        ),
    )
    val dummyShipping = listOf(
        ShippingModel(
            name = "REG",
            price = 13.00,
            description = "Estimated time of arrival 2 - 3 days"
        ),
        ShippingModel(
            name = "OKE",
            price = 15.00,
            description = "Estimated time of arrival 1 - 2 days"
        ),
        ShippingModel(
            name = "YES",
            price = 10.00,
            description = "Estimated time of arrival 1 - 2 days"
        )
    )
    val dummyPaymentMethod = listOf(
        PaymentMethodModel(
            icon = R.drawable.icon_discover,
            name = "Discover"
        ),
        PaymentMethodModel(
            icon = R.drawable.icon_master_card,
            name = "Master Card"
        ),
        PaymentMethodModel(
            icon = R.drawable.icon_paypal,
            name = "Paypal"
        ),
        PaymentMethodModel(
            icon = R.drawable.icon_visa,
            name = "Visa"
        ),
    )
    val dummyCoupon = listOf(
        CouponModel(
            discountedPrice = "FREE SHIPPING",
            description = "Applies to get free shipping",
            expiredDate = "31 Desember 2024",
            color1 = Color(0xFF9733EE),
            color2 = Color(0xFFDA22FF),
            couponCode = "FWHWFW45A"
        ),
        CouponModel(
            discountedPrice = "25%",
            description = "Applies to get 25% off",
            expiredDate = "31 Desember 2024",
            color1 = Color(0xFFFFA726),
            color2 = Color(0xFFFFD54F),
            couponCode = "ADERTS4TA"
        ),
        CouponModel(
            discountedPrice = "50%",
            couponCode = "NHJASE32Q",
            description = "Applies to get 50% off",
            expiredDate = "31 Desember 2024",
            color1 = Color(0xFF00C9FF),
            color2 = Color(0xFF92FE9D)
        )
    )
    // Tạo một số CartModel mẫu
    val cartItems = listOf(
        CartModel(
            id = 1,
            productId = 101,
            productName = "Sản phẩm A",
            productPrice = "1000000.0",
            productImage = "url_to_image_1",
            productCategory = "Điện tử",
            productQuantity = 2
        ),
        CartModel(
            id = 2,
            productId = 102,
            productName = "Sản phẩm B",
            productPrice = "1500000.0",
            productImage = "url_to_image_2",
            productCategory = "Điện tử",
            productQuantity = 1
        )
    )

    // Tạo đối tượng OrderModel
    val order = OrderModel(
        id = 1,
        orderCode = "ORD12345",
        totalPrice = 3500000.0, // Tổng giá trị đơn hàng
        orderDate = Timestamp.now(), // Lấy thời gian hiện tại làm ngày đặt hàng
        items = cartItems
    )
    val listOrder = listOf(
        OrderModel(
            id = 1,
            orderCode = "ORD12345",
            totalPrice = 3500000.0, // Tổng giá trị đơn hàng
            orderDate = Timestamp.now(), // Lấy thời gian hiện tại làm ngày đặt hàng
            items = cartItems
        ),
        OrderModel(
            id = 2,
            orderCode = "ORD12345",
            totalPrice = 3500000.0, // Tổng giá trị đơn hàng
            orderDate = Timestamp.now(), // Lấy thời gian hiện tại làm ngày đặt hàng
            items = cartItems
        ),
        OrderModel(
            id = 3,
            orderCode = "ORD12345",
            totalPrice = 3500000.0, // Tổng giá trị đơn hàng
            orderDate = Timestamp.now(), // Lấy thời gian hiện tại làm ngày đặt hàng
            items = cartItems
        ),
        OrderModel(
            id = 4,
            orderCode = "ORD12345",
            totalPrice = 3500000.0, // Tổng giá trị đơn hàng
            orderDate = Timestamp.now(), // Lấy thời gian hiện tại làm ngày đặt hàng
            items = cartItems
        ),
        OrderModel(
            id = 5,
            orderCode = "ORD12345",
            totalPrice = 3500000.0, // Tổng giá trị đơn hàng
            orderDate = Timestamp.now(), // Lấy thời gian hiện tại làm ngày đặt hàng
            items = cartItems
        )
    )
}