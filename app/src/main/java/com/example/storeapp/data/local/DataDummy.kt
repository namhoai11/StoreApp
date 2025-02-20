package com.example.storeapp.data.local

import androidx.compose.ui.graphics.Color
import com.example.storeapp.R
import com.example.storeapp.model.AvailableOptions
import com.example.storeapp.model.BrandModel
import com.example.storeapp.model.CartModel
import com.example.storeapp.model.ColorOptions
import com.example.storeapp.model.CouponActive
import com.example.storeapp.model.CouponModel
import com.example.storeapp.model.CouponType
import com.example.storeapp.model.District
import com.example.storeapp.model.OrderModel
import com.example.storeapp.model.PaymentMethodModel
import com.example.storeapp.model.ProductDataForOrderModel
import com.example.storeapp.model.ProductModel
import com.example.storeapp.model.ProductOptions
import com.example.storeapp.model.ProductsOnCart
import com.example.storeapp.model.Province
import com.example.storeapp.model.ShippingModel
import com.example.storeapp.model.StockByVariant
import com.example.storeapp.model.UserLocationModel
import com.example.storeapp.model.Ward
import com.example.storeapp.ui.screen.address.AddressUiState
import com.example.storeapp.ui.screen.address.add_address.AddAddressUiState
import com.example.storeapp.ui.screen.admin.manage.coupon.CouponManagementUiState
import com.example.storeapp.ui.screen.admin.manage.coupon.add_coupon.AddCouponUiState
import com.example.storeapp.ui.screen.cart.ProductsOnCartToShow
import com.example.storeapp.ui.screen.checkout.CheckoutUiState
import com.google.firebase.Timestamp

object DataDummy {
    // Dữ liệu mẫu cho địa chỉ người dùng
    val dummyUserLocation = listOf(
        UserLocationModel(
            id = "1",
            userName = "La Hoai Nam",
            street = "Jl. Durian No. 123",
            province = "Jawa Tengah",
            district = "Kab. Semarang",
            ward = "Banyubiru",
//            isDefault = true,
            userId = "user123",
            provinceId = "province01",
            districtId = "district01",
            wardId = "ward01",
//            latitude = -7.123456,
//            longitude = 110.123456,
//            createdAt = Timestamp.now(),
//            updatedAt = Timestamp.now()
        ),
        UserLocationModel(
            id = "2",
            userName = "La Hoai Nam",
            street = "Jl. Durian No. 124",
            province = "Jawa Tengah",
            district = "Kab. Semarang",
            ward = "Banyubiru",
//            isDefault = false,
            userId = "user123",
            provinceId = "province01",
            districtId = "district01",
            wardId = "ward01",
//            latitude = -7.654321,
//            longitude = 110.654321,
//            createdAt = Timestamp.now(),
//            updatedAt = Timestamp.now()
        ),
    )
    val addressUiState = AddressUiState(
        addressList = dummyUserLocation,
        selectedItemId = "2",
    )
    val addAddressUiState = AddAddressUiState(
        provinces = listOf(
            Province(1, "Thành phố Hà Nội"),
            Province(2, "Tỉnh Hà Giang")
        ),
        districts = listOf(
            District(1, "Quận Ba Đình"),
            District(2, "Huyện Gia Lâm")
        ),
        wards = listOf(
            Ward(1, "Phường Quảng An"),
            Ward(2, "Phường Giang Biên")
        ),
        selectedProvince = Province(1, "Thành phố Hà Nội"),
        selectedDistrict = District(1, "Quận Ba Đình"),
        selectedWard = Ward(1, "Phường Quảng An"),
        street = "Hồ Tây",
        isLoading = false,
        errorMessage = null
    )

    // Dữ liệu mẫu cho phương thức vận chuyển
    val dummyShipping = listOf(
        ShippingModel(
            name = "Nhanh",
            price = 13.00,
            description = "Thời gian dự kiến đến: 2 - 3 ngày"
        ),
        ShippingModel(
            name = "Hỏa tốc",
            price = 15.00,
            description = "Thời gian dự kiến đến: 1 - 2 ngày"
        ),
        ShippingModel(
            name = "Tiết kiệm",
            price = 10.00,
            description = "Thời gian dự kiến đến: 1 - 2 ngày"
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


    val coupon = CouponModel(
        id = "1",
        code = "FWHWFW45A",
        name = "Free Shipping",
        description = "Applies to get free shipping",
        type = CouponType.PERCENTAGE,
        value = 0.5,
        maxDiscount = null,
        minOrderValue = null,
        quantity = 1,
                    active = CouponActive.ALL,
        startDate = Timestamp(1706774400, 0),// 01/02/2024 08:00:00
        endDate = Timestamp(1707570600, 0),// 10/02/2024 18:30:00
        color1 = Color(0xFF9733EE),
        color2 = Color(0xFFDA22FF)
    )

    // Dữ liệu mẫu cho Coupon
    val dummyCoupon = listOf(
        CouponModel(
            id = "1",
            code = "FWHWFW45A",
            name = "Free Shipping",
            description = "Applies to get free shipping",
            type = CouponType.FIXED_AMOUNT,
            value = 50000.0,
            maxDiscount = null,
            minOrderValue = null,
            quantity = 1,
                        active = CouponActive.ALL,
            startDate = Timestamp(1706774400, 0),// 01/02/2024 08:00:00
            endDate = Timestamp(1707570600, 0),// 10/02/2024 18:30:00
            color1 = Color(0xFF9733EE),
            color2 = Color(0xFFDA22FF)
        ),
        CouponModel(
            id = "2",
            code = "ADERTS4TA",
            name = "25% Off",
            description = "Applies to get 25% off",
            type = CouponType.PERCENTAGE,
            value = 0.25,
            maxDiscount = null,
            minOrderValue = 100.0,
            quantity = 1,
                        active = CouponActive.ALL,
            startDate = Timestamp(1706774400, 0),// 01/02/2024 08:00:00
            endDate = Timestamp(1707570600, 0),// 10/02/2024 18:30:00
            color1 = Color(0xFFFFA726),
            color2 = Color(0xFFFFD54F)
        ),
        CouponModel(
            id = "3",
            code = "NHJASE32Q",
            name = "50% Off",
            description = "Applies to get 50% off",
            type = CouponType.PERCENTAGE,
            value = 50.0,
            maxDiscount = null,
            minOrderValue = 50.0,
            quantity = 1,
                        active = CouponActive.ALL,
            startDate = Timestamp(1706774400, 0),// 01/02/2024 08:00:00
            endDate = Timestamp(1707570600, 0),// 10/02/2024 18:30:00
            color1 = Color(0xFF00C9FF),
            color2 = Color(0xFF92FE9D)
        )
    )

    val couponManagementUiState = CouponManagementUiState(
        listCoupon = dummyCoupon,
        currentCouponActive = CouponActive.ONGOING,
        listCouponByActive = mapOf(
            CouponActive.ONGOING to listOf(
                CouponModel(
                    id = "1",
                    code = "FWHWFW45A",
                    name = "Free Shipping",
                    description = "Applies to get free shipping",
                    type = CouponType.FIXED_AMOUNT,
                    value = 50000.0,
                    maxDiscount = null,
                    minOrderValue = null,
                    quantity = 1,
                                active = CouponActive.ALL,
                    startDate = Timestamp(1706774400, 0),// 01/02/2024 08:00:00
                    endDate = Timestamp(1707570600, 0),// 10/02/2024 18:30:00
                    color1 = Color(0xFF9733EE),
                    color2 = Color(0xFFDA22FF)
                )
            ),
            CouponActive.UPCOMING to listOf(
                CouponModel(
                    id = "2",
                    code = "ADERTS4TA",
                    name = "25% Off",
                    description = "Applies to get 25% off",
                    type = CouponType.PERCENTAGE,
                    value = 0.25,
                    maxDiscount = null,
                    minOrderValue = 100.0,
                    quantity = 1,
                                active = CouponActive.ALL,
                    startDate = Timestamp(1706774400, 0),// 01/02/2024 08:00:00
                    endDate = Timestamp(1707570600, 0),// 10/02/2024 18:30:00
                    color1 = Color(0xFFFFA726),
                    color2 = Color(0xFFFFD54F)
                )
            ),
            CouponActive.EXPIRED to listOf(
                CouponModel(
                    id = "3",
                    code = "NHJASE32Q",
                    name = "50% Off",
                    description = "Applies to get 50% off",
                    type = CouponType.PERCENTAGE,
                    value = 50.0,
                    maxDiscount = null,
                    minOrderValue = 50.0,
                    quantity = 1,
                                active = CouponActive.ALL,
                    startDate = Timestamp(1706774400, 0),// 01/02/2024 08:00:00
                    endDate = Timestamp(1707570600, 0),// 10/02/2024 18:30:00
                    color1 = Color(0xFF00C9FF),
                    color2 = Color(0xFF92FE9D)
                )
            )
        ),
        isLoading = false,
        errorMessage = null,
        successMessage = "Dữ liệu tải thành công!"
    )
    val addCouponUiState = AddCouponUiState(
        couponDetailsItem = CouponModel(
            id = "COUPON123",
            code = "SALE50",
            name = "Giảm giá 50%",
            description = "Áp dụng cho đơn hàng từ 500K",
            type = CouponType.PERCENTAGE,
            value = 50.0,
            maxDiscount = 100.0,
            minOrderValue = 500.0,
            quantity = 10,
                        active = CouponActive.ALL,
            startDate = Timestamp.now(),
            endDate = Timestamp.now(),
            color1 = Color.Blue,
            color2 = Color.Cyan
        ),
        isLoading = false,
        errorMessage = null,
        successMessage = "Coupon đã được tạo thành công!"
    )


    val productsOnCart = ProductsOnCart(
        productId = "101",
        productName = "Sản phẩm A",
        productImage = "url_to_image_1",
        quantity = 2
    )

    val productsOnCartToShow = ProductsOnCartToShow(
        productId = "101",
        productName = "Sản phẩm A",
        productImage = "url_to_image_1",
        productPrice = 50000.0,
        productOptions = "Size L",
        colorOptions = "Đỏ",
        quantity = 2,
        remainingStock = 5,
        notExist = "",
        notEnough = ""
    )

    val listProductsOnCartToShow = listOf(
        productsOnCartToShow,
        productsOnCartToShow,
        productsOnCartToShow
    )

    val checkoutUiState = CheckoutUiState(
        selectedLocation = UserLocationModel(
            id = "1",
            userName = "La Hoai Nam",
            street = "Jl. Durian No. 123",
            province = "Jawa Tengah",
            district = "Kab. Semarang",
            ward = "Banyubiru",
            userId = "user123",
            provinceId = "province01",
            districtId = "district01",
            wardId = "ward01",
        ),
        products = listProductsOnCartToShow,
        totalPrice = 90000.0,
        oldTotalPrice = 100000.0,
        finalPrice = 75000.0, // Giả sử có giảm giá
        isShowDialog = false,
        isChooseShipping = false,
        isChooseCoupon = false,
        listShipping = dummyShipping,
        listCoupon = dummyCoupon,
        selectedShipping = ShippingModel(
            name = "Nhanh",
            price = 13.00,
            description = "Thời gian dự kiến đến: 2 - 3 ngày"
        ),
        selectedCoupon = CouponModel(
            id = "1",
            code = "FWHWFW45A",
            name = "Free Shipping",
            description = "Applies to get free shipping",
            type = CouponType.PERCENTAGE,
            value = 0.0,
            maxDiscount = null,
            minOrderValue = null,
            quantity = 1,
            active = CouponActive.ALL,
            startDate = Timestamp(1706774400, 0),// 01/02/2024 08:00:00
            endDate = Timestamp(1707570600, 0),// 10/02/2024 18:30:00
            color1 = Color(0xFF9733EE),
            color2 = Color(0xFFDA22FF)
        ),
        note = "Giao hàng ngoài giờ hành chính",
        selectedPaymentMethod = "COD",
        isButtonEnabled = true, // Giả sử nút đặt hàng có thể bấm
        isLoading = false,
        errorMessage = "",
        successMessage = "Đặt hàng thành công"
    )

    // Dữ liệu mẫu cho Cart
    val cartItems = CartModel(
        id = "1",
        products = listOf(
            ProductsOnCart(
                productId = "101",
                productName = "Sản phẩm A",
                productImage = "url_to_image_1",
                quantity = 2
            ),
            ProductsOnCart(
                productId = "101",
                productName = "Sản phẩm A",
                productImage = "url_to_image_1",
                quantity = 2
            )
        ),
        userId = "user123"
    )
    val order = OrderModel(
        userId = "user123",
        products = listOf(
            ProductsOnCartToShow(
                productId = "101",
                productName = "Sản phẩm A",
                productImage = "url_to_image_1",
                productPrice = 50000.0,
                productOptions = "Size L",
                colorOptions = "Đỏ",
                quantity = 2,
                remainingStock = 5,
                notExist = "",
                notEnough = ""
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
            userId = "user123",
            products = listOf(
                ProductsOnCartToShow(
                    productId = "101",
                    productName = "Sản phẩm A",
                    productImage = "url_to_image_1",
                    productPrice = 50000.0,
                    productOptions = "Size L",
                    colorOptions = "Đỏ",
                    quantity = 2,
                    remainingStock = 5,
                    notExist = "",
                    notEnough = ""
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
            userId = "user123",
            products = listOf(
                ProductsOnCartToShow(
                    productId = "101",
                    productName = "Sản phẩm A",
                    productImage = "url_to_image_1",
                    productPrice = 50000.0,
                    productOptions = "Size L",
                    colorOptions = "Đỏ",
                    quantity = 2,
                    remainingStock = 5,
                    notExist = "",
                    notEnough = ""
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

    val listProductOptions = listOf(
        ProductOptions(
            optionsName = "core i3",
            priceForOptions = 555.0
        ),
        ProductOptions(
            optionsName = "core i3",
            priceForOptions = 555.0
        ),
        ProductOptions(
            optionsName = "core i3",
            priceForOptions = 555.0
        )
    )
    val listColorOptions = listOf(
        ColorOptions(
            colorName = "Red",
            imagesColor = "https://firebasestorage.googleapis.com/v0/b/commerc-b4186.appspot.com/o/cat1_black.png?alt=media&token=85627f03-9075-43cf-af01-06e96bdd1a77"
        ),
        ColorOptions(
            colorName = "Blue",
            imagesColor = "https://firebasestorage.googleapis.com/v0/b/commerc-b4186.appspot.com/o/cat1_black.png?alt=media&token=85627f03-9075-43cf-af01-06e96bdd1a77"
        ),
        ColorOptions(
            colorName = "Pink",
            imagesColor = "https://firebasestorage.googleapis.com/v0/b/commerc-b4186.appspot.com/o/cat1_black.png?alt=media&token=85627f03-9075-43cf-af01-06e96bdd1a77"
        ),
        ColorOptions(
            colorName = "Yellow",
            imagesColor = "https://firebasestorage.googleapis.com/v0/b/commerc-b4186.appspot.com/o/cat1_black.png?alt=media&token=85627f03-9075-43cf-af01-06e96bdd1a77"
        )

    )

    val availableOptions = AvailableOptions(
        listProductOptions = listProductOptions,
        listColorOptions = listColorOptions
    )

    val productItem = ProductModel(
        id = "1",
        name = "Business Laptop",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed  do eiusmod tempor incididunt ut labore et dolore magna  aliqua. Ut enim ad minim veniam, quis nostrud exercitation  ullamco laboris nisi ut aliquip ex ea commodo consequat.  Duis aute irure dolor in reprehenderit in voluptate velit esse  cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat  cupidatat non proident, sunt in culpa qui officia deserunt .Excepteur sint occaecat",
        images = arrayListOf(
            "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_1.png?alt=media&token=fb49a7c9-3094-4f5c-9ea6-b8365cd86323",
            "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_2.png?alt=media&token=3f826014-4808-4387-af6f-22dc7ddd4780",
            "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_3.png?alt=media&token=d4ab793a-cb72-45ab-ae43-8db69adaaeba",
            "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_4.png?alt=media&token=dfb10462-9138-471a-b34a-537bc7f5b7c8",
            "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_5.png?alt=media&token=2bfd17ef-d8c5-409e-8d6c-2d9e57d394c4"
        ),
        options = arrayListOf(
            "core i3",
            "core i5",
            "core i7"
        ),
        availableOptions = availableOptions,
        price = 550.0,
        rating = 4.7,
        categoryId = "0",
        hidden = false,
        stockQuantity = 12,
        createdAt = Timestamp.now(),
        updatedAt = Timestamp.now(),
    )
}
