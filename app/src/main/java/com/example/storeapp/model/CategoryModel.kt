package com.example.storeapp.model

import com.google.firebase.Timestamp

//data class CategoryModel(
//    val title: String = "",
//    val id: Int = 0,
//    val picUrl: String = ""
//)

data class CategoryModel(
    val id: Int = 0,
    val name: String,
    val imageUrl: String,
    val description: String = "",
    val hidden: Boolean = false,
    val productCount: Int = 1,
    val createdAt: Timestamp,
    val updatedAt: Timestamp
//    val createdAt: Timestamp,
//    val updatedAt: Timestamp
)
