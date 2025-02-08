package com.example.storeapp.data.local

import androidx.compose.ui.graphics.Color
import com.example.storeapp.R
import com.example.storeapp.model.AvailableOptions
import com.example.storeapp.model.BrandModel
import com.example.storeapp.model.CartModel
import com.example.storeapp.model.ColorOptions
import com.example.storeapp.model.CouponModel
import com.example.storeapp.model.OrderModel
import com.example.storeapp.model.PaymentMethodModel
import com.example.storeapp.model.ProductDataForOrderModel
import com.example.storeapp.model.ProductModel
import com.example.storeapp.model.ProductOptions
import com.example.storeapp.model.ProductsOnCart
import com.example.storeapp.model.ShippingModel
import com.example.storeapp.model.StockByVariant
import com.example.storeapp.model.UserLocationModel
import com.google.firebase.Timestamp

object DataDummy {
    // Dữ liệu mẫu cho địa chỉ người dùng
    val dummyUserLocation = listOf(
        UserLocationModel(
            id = "1",
            street = "Jl. Durian No. 123",
            province = "Jawa Tengah",
            district = "Kab. Semarang",
            ward = "Banyubiru",
            isDefault = true,
            userId = "user123",
            provinceId = "province01",
            districtId = "district01",
            wardId = "ward01",
            latitude = -7.123456,
            longitude = 110.123456,
            createdAt = Timestamp.now(),
            updatedAt = Timestamp.now()
        ),
        UserLocationModel(
            id = "2",
            street = "Jl. Durian No. 124",
            province = "Jawa Tengah",
            district = "Kab. Semarang",
            ward = "Banyubiru",
            isDefault = false,
            userId = "user123",
            provinceId = "province01",
            districtId = "district01",
            wardId = "ward01",
            latitude = -7.654321,
            longitude = 110.654321,
            createdAt = Timestamp.now(),
            updatedAt = Timestamp.now()
        ),
    )

    // Dữ liệu mẫu cho phương thức vận chuyển
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

    // Dữ liệu mẫu cho phương thức thanh toán
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

    // Dữ liệu mẫu cho Coupon
    val dummyCoupon = listOf(
        CouponModel(
            id = "1",
            code = "FWHWFW45A",
            name = "Free Shipping",
            description = "Applies to get free shipping",
            type = "FIXED_AMOUNT",
            value = 0.0,
            maxDiscount = null,
            minOrderValue = null,
            quantity = 1,
            isActive = true,
            startDate = Timestamp.now(),
            endDate = Timestamp.now(),
            color1 = Color(0xFF9733EE),
            color2 = Color(0xFFDA22FF)
        ),
        CouponModel(
            id = "2",
            code = "ADERTS4TA",
            name = "25% Off",
            description = "Applies to get 25% off",
            type = "PERCENTAGE",
            value = 25.0,
            maxDiscount = null,
            minOrderValue = 100.0,
            quantity = 1,
            isActive = true,
            startDate = Timestamp.now(),
            endDate = Timestamp.now(),
            color1 = Color(0xFFFFA726),
            color2 = Color(0xFFFFD54F)
        ),
        CouponModel(
            id = "3",
            code = "NHJASE32Q",
            name = "50% Off",
            description = "Applies to get 50% off",
            type = "PERCENTAGE",
            value = 50.0,
            maxDiscount = null,
            minOrderValue = 50.0,
            quantity = 1,
            isActive = true,
            startDate = Timestamp.now(),
            endDate = Timestamp.now(),
            color1 = Color(0xFF00C9FF),
            color2 = Color(0xFF92FE9D)
        )
    )

    // Dữ liệu mẫu cho Cart
    val cartItems = listOf(
        CartModel(
            id = "1",
            products = listOf(
                ProductsOnCart(
                    productId = "101",
                    productName = "Sản phẩm A",
                    productImage = "url_to_image_1",
                    productPrice = 1000000.0,
                    quantity = 2
                )
            ),
            total = 2000000.0,
            userId = "user123"
        ),
        CartModel(
            id = "2",
            products = listOf(
                ProductsOnCart(
                    productId = "102",
                    productName = "Sản phẩm B",
                    productImage = "url_to_image_2",
                    productPrice = 1500000.0,
                    quantity = 1
                )
            ),
            total = 1500000.0,
            userId = "user123"
        )
    )
    val order = OrderModel(
        id = "1",
        userId = "user123",
        products = listOf(
            ProductDataForOrderModel(
                id = "101",
                product = ProductModel(
                    id = "101",
                    name = "Sản phẩm A",
                    images = listOf("url_to_image_1"),
                    price = 1000000.0,
                    stockQuantity = 50,
                    brandId = "brand01",
                    categoryId = "category01",
                    hidden = false,
                    description = "Product A description",
                    rating = 4.5,
                    availableOptions = AvailableOptions( // ✅ Sửa đúng kiểu dữ liệu
                        listProductOptions = listOf(
                            ProductOptions("Option1", 100000.0),
                            ProductOptions("Option2", 200000.0)
                        ),
                        listColorOptions = listOf(
                            ColorOptions("Red", "url_to_image_red"),
                            ColorOptions("Blue", "url_to_image_blue")
                        )
                    ),
                    options = listOf("Option1"),
                    stockByVariant = listOf( // ✅ Thêm danh sách tồn kho theo biến thể
                        StockByVariant(colorName = "Red", optionName = "64GB", quantity = 20),
                        StockByVariant(colorName = "Red", optionName = "128GB", quantity = 15),
                        StockByVariant(colorName = "Blue", optionName = "64GB", quantity = 10),
                        StockByVariant(colorName = "Blue", optionName = "128GB", quantity = 5)
                    ),
                    createdAt = Timestamp.now(),
                    updatedAt = Timestamp.now(),
                    brand = BrandModel(
                        id = "brand01",
                        name = "Brand A",
                        imageUrl = "",
                        description = "Brand A",
                        hidden = false,
                        createdAt = Timestamp.now(),
                        updatedAt = Timestamp.now()
                    )
                ),
                orderId = "ORD12345",
                quantity = 2
            )
        ),
        totalPrice = 3500000.0,
        status = "Pending",
        orderCode = "VoHuyenTram",
        paymentMethod = "Credit Card",
        addressId = "address123",
        createdAt = Timestamp.now(),
        updatedAt = Timestamp.now()
    )

    // Dữ liệu mẫu cho Order
    val listOrder = listOf(
        OrderModel(
            id = "1",
            userId = "user123",
            products = listOf(
                ProductDataForOrderModel(
                    id = "101",
                    product = ProductModel(
                        id = "101",
                        name = "Sản phẩm A",
                        images = listOf("url_to_image_1"),
                        price = 1000000.0,
                        stockQuantity = 50,
                        brandId = "brand01",
                        categoryId = "category01",
                        hidden = false,
                        description = "Product A description",
                        rating = 4.5,
                        availableOptions = AvailableOptions( // ✅ Sửa đúng kiểu dữ liệu
                            listProductOptions = listOf(
                                ProductOptions("Option1", 100000.0),
                                ProductOptions("Option2", 200000.0)
                            ),
                            listColorOptions = listOf(
                                ColorOptions("Red", "url_to_image_red"),
                                ColorOptions("Blue", "url_to_image_blue")
                            )
                        ),
                        options = listOf("Option1"),
                        stockByVariant = listOf( // ✅ Thêm danh sách tồn kho theo biến thể
                            StockByVariant(colorName = "Red", optionName = "64GB", quantity = 20),
                            StockByVariant(colorName = "Red", optionName = "128GB", quantity = 15),
                            StockByVariant(colorName = "Blue", optionName = "64GB", quantity = 10),
                            StockByVariant(colorName = "Blue", optionName = "128GB", quantity = 5)
                        ),
                        createdAt = Timestamp.now(),
                        updatedAt = Timestamp.now(),
                        brand = BrandModel(
                            id = "brand01",
                            name = "Brand A",
                            imageUrl = "",
                            description = "Brand A",
                            hidden = false,
                            createdAt = Timestamp.now(),
                            updatedAt = Timestamp.now()
                        )
                    ),
                    orderId = "ORD12345",
                    quantity = 2
                )
            ),
            totalPrice = 3500000.0,
            status = "Pending",
            orderCode = "VoHuyenTram",
            paymentMethod = "Credit Card",
            addressId = "address123",
            createdAt = Timestamp.now(),
            updatedAt = Timestamp.now()
        ),
        OrderModel(
            id = "1",
            userId = "user123",
            products = listOf(
                ProductDataForOrderModel(
                    id = "101",
                    product = ProductModel(
                        id = "101",
                        name = "Sản phẩm A",
                        images = listOf("url_to_image_1"),
                        price = 1000000.0,
                        stockQuantity = 50,
                        brandId = "brand01",
                        categoryId = "category01",
                        hidden = false,
                        description = "Product A description",
                        rating = 4.5,
                        availableOptions = AvailableOptions( // ✅ Sửa đúng kiểu dữ liệu
                            listProductOptions = listOf(
                                ProductOptions("Option1", 100000.0),
                                ProductOptions("Option2", 200000.0)
                            ),
                            listColorOptions = listOf(
                                ColorOptions("Red", "url_to_image_red"),
                                ColorOptions("Blue", "url_to_image_blue")
                            )
                        ),
                        options = listOf("Option1"),
                        stockByVariant = listOf( // ✅ Thêm danh sách tồn kho theo biến thể
                            StockByVariant(colorName = "Red", optionName = "64GB", quantity = 20),
                            StockByVariant(colorName = "Red", optionName = "128GB", quantity = 15),
                            StockByVariant(colorName = "Blue", optionName = "64GB", quantity = 10),
                            StockByVariant(colorName = "Blue", optionName = "128GB", quantity = 5)
                        ),
                        createdAt = Timestamp.now(),
                        updatedAt = Timestamp.now(),
                        brand = BrandModel(
                            id = "brand01",
                            name = "Brand A",
                            imageUrl = "",
                            description = "Brand A",
                            hidden = false,
                            createdAt = Timestamp.now(),
                            updatedAt = Timestamp.now()
                        )
                    ),
                    orderId = "ORD12345",
                    quantity = 2
                )
            ),
            totalPrice = 3500000.0,
            status = "Pending",
            orderCode = "VoHuyenTram",
            paymentMethod = "Credit Card",
            addressId = "address123",
            createdAt = Timestamp.now(),
            updatedAt = Timestamp.now()
        )
    )
}
