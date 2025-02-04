package com.example.storeapp.model

import com.google.firebase.Timestamp


data class ProductModel(
    val id: String = "",
    val name: String = "",
    val images: List<String> = emptyList(),
    val price: Double = 0.0,
    val stockQuantity: Int = 0,
    val brandId: String? = null,
    val categoryId: String = "",
    val hidden: Boolean = false,
    var showRecommended: Boolean = false,
    val description: String = "",
    val rating: Double = 0.0,
    val availableOptions: ProductOptions? = null,
    val options: List<String> = emptyList(),
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now(),
    val brand: BrandModel? = null
) {
    constructor() : this("", "", emptyList(), 0.0, 0, null, "", false, false, "", 0.0, null, emptyList(), Timestamp.now(), Timestamp.now(), null)
}

data class ProductOptions(
    val chip: List<String> = emptyList(),
    val ram: List<String> = emptyList(),
    val rom: List<String> = emptyList()
) {
    constructor() : this(emptyList(), emptyList(), emptyList())
}

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
