package com.example.storeapp.model

data class ItemsModel(
    var id: Int = 0,
    var title: String = "",
    var description: String = "",
    var picUrl: ArrayList<String> = ArrayList(),
    var model: ArrayList<String> = ArrayList(),
    var price: Double = 0.0,
    var rating: Double = 0.0,
//    var numberInCart: Int = 0,
    var showRecommended: Boolean = false,
    var categoryId: String = ""
)
