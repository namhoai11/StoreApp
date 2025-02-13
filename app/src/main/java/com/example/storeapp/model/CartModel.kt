package com.example.storeapp.model


data class CartModel(
    val id: String,
    val products: List<ProductsOnCart>,
    val userId: String
) {
    // Constructor mặc định (nếu cần)
    constructor() : this("", emptyList(), "")

//    // Constructor tính tổng giá từ danh sách sản phẩm
//    constructor(id: String, products: List<ProductsOnCart>, userId: String) : this(
//        id,
//        products,
//        userId
//    )
}

data class ProductsOnCart(
    val productId: String,
    val productName: String,
    val productImage: String,
//    val productPrice: Double,
//    val productOptions: ProductOptions? = null,
//    val colorOptions: ColorOptions? = null,
    val productOptions: String = "",
    val colorOptions: String = "",
    val quantity: Int
) {
    // Constructor mặc định (nếu cần)
    constructor() : this("", "", "", "", "", 0)

//    // Constructor không có `productOptions` và `colorOptions`
//    constructor(
//        productId: String,
//        productName: String,
//        productImage: String,
//        productOptions: String,
//        colorOptions: String,
//        quantity: Int
//    ) : this(productId, productName, productImage,productOptions , colorOptions, quantity)
}

