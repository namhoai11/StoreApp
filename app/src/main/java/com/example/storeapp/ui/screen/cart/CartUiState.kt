package com.example.storeapp.ui.screen.cart


data class CartUiState(
    val listProductOnCart: List<ProductsOnCartToShow> = emptyList(),
    val totalPrice: Double = 0.0,
    val showCartLoading: Boolean = false,
    val errorMessage: String = "",
    val successMessage: String = "",
    val isShowConfirmRemovedDialog: Boolean = false,
    val productSelected: ProductsOnCartToShow? = null
)

sealed class CartAction {
    data object Increase : CartAction()
    data object Decrease : CartAction()
    data object Remove : CartAction()
}

data class ProductsOnCartToShow(
    val productId: String,
    val productName: String,
    val productImage: String,
    val productPrice: Double,
    val productOptions: String = "",
    val colorOptions: String = "",
    val quantity: Int,
    val productTotalPrice: Double = 0.0,
    val notExist: String = "",
    val notEnough: String = "",
) {
    // Constructor mặc định (nếu cần)
    constructor() : this("", "", "", 0.0, "", "", 0, 0.0, "", "")

    // Constructor không có `productOptions` và `colorOptions`
    constructor(
        productId: String,
        productName: String,
        productImage: String,
        productPrice: Double,
        productOptions: String,
        colorOptions: String,
        quantity: Int,
        notExist: String,
        notEnough: String
    ) : this(
        productId,
        productName,
        productImage,
        productPrice,
        productOptions,
        colorOptions,
        quantity,
        productPrice * quantity,
        notExist,
        notEnough
    )
}