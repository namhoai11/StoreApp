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
    val productOptions: ProductOptions? = null,
    val colorOptions: ColorOptions? = null,
    val quantity: Int
) {
    // Constructor mặc định (nếu cần)
    constructor() : this("", "", "", null, null, 0)

    // Constructor không có `productOptions` và `colorOptions`
    constructor(
        productId: String,
        productName: String,
        productImage: String,
        quantity: Int
    ) : this(productId, productName, productImage, null, null, quantity)
}

