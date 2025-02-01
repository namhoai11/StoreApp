package com.example.storeapp.data.repository

import com.example.storeapp.model.CategoryModel
import com.example.storeapp.model.ProductModel
import com.example.storeapp.model.SliderModel

interface StoreAppRepository {
    suspend fun loadBanner(): List<SliderModel>
    suspend fun loadAllItems(): List<ProductModel>
    suspend fun loadCategory(): List<CategoryModel>
//    suspend fun loadRecommended(): List<ItemsModel>
//    suspend fun loadFiltered(categoryId: String): List<ItemsModel>
}