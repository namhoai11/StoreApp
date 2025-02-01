package com.example.storeapp.model

//data class FavoriteModel(
//    var id: Int = 0,
//    var productId: Int = 0,
//    var productName: String = "",
//    var productPrice: String = "",
//    var productImage: String = "",
//    var productCategory: String = "",
//    var productQuantity: Int = 0
//)

data class FavoriteModel(
    val productId: String,
    val productName: String,
    val productPrice: Double,
    val productImage: String,
    val productCategory: String,
    val productQuantity: Int
)
data class WishlistModel(
    val id: String,               // ID của danh sách yêu thích
    val userId: String,           // ID người dùng
    val products: List<FavoriteModel> // Danh sách các sản phẩm yêu thích
)