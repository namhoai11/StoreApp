package com.example.storeapp.model

import com.google.firebase.Timestamp

data class BrandModel(
    val id: String,
    val name: String,
    val imageUrl: String,
    val description: String,
    val hidden: Boolean,
    val createdAt: Timestamp,
    val updatedAt: Timestamp
)
