package com.example.storeapp.model

import com.google.firebase.Timestamp


data class ProductModel(
    val id: String,
    val name: String,
    val images: List<String>,
    val price: Double,
    val stockQuantity: Int,
    val brandId: String? = null,
    val categoryId: String,
    val hidden: Boolean,
    val description: String,
    val rating: Double,
    val availableOptions: ProductOptions? = null,  // List các tùy chọn có sẵn
    val options: List<String>,             // List các tùy chọn chung (chip, ram, rom, v.v.)
    val createdAt: Timestamp,
    val updatedAt: Timestamp,
    val brand: BrandModel? = null
)

data class ProductOptions(
    val chip: List<String>,   // Các tùy chọn cho chip
    val ram: List<String>,    // Các tùy chọn cho ram
    val rom: List<String>     // Các tùy chọn cho rom
)

//data class ProductModel(
//    var id: Int = 0,
//    var title: String = "",
//    var description: String = "",
//    var picUrl: ArrayList<String> = ArrayList(),
//    var model: ArrayList<String> = ArrayList(),
//    var price: Double = 0.0,
//    var rating: Double = 0.0,
////    var numberInCart: Int = 0,
//    var showRecommended: Boolean = false,
//    var categoryId: String = ""
//)
