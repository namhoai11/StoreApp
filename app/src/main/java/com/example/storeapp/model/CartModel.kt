package com.example.storeapp.model


data class CartModel(
    val id: String = "",
    val products: List<ProductsOnCart> = emptyList(),
    val total: Double = 0.0,
    val userId: String = ""
)

data class ProductsOnCart(
    val productId: String,
    val productName: String,
    val productImage: String,
    val productPrice: Double,
    val productOptions: ProductOptions? = null,
    val colorOptions: ColorOptions? = null,
    val quantity: Int
)

//data class CartModel(
//    var id: Int = 0,
//    var productId: Int = 0,
//    var productName: String = "",
//    var productPrice: String = "",
//    var productImage: String = "",
//    var productCategory: String = "",
//    var productQuantity: Int = 0
//)

