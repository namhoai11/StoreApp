package com.example.storeapp.model

import com.google.firebase.Timestamp

//data class CategoryModel(
//    val title: String = "",
//    val id: Int = 0,
//    val picUrl: String = ""
//)

data class CategoryModel(
    val id: String = "",
    val name: String = "",
    val imageUrl: String = "",
    val description: String = "",
    val hidden: Boolean = false,
    val productCount: Int = 1,
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now()
//    val createdAt: Timestamp,
//    val updatedAt: Timestamp
) {
    constructor() : this("", "", "", "", false, 1, Timestamp.now(), Timestamp.now())
}
