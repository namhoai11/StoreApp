package com.example.storeapp.data.repository

import android.util.Log
import com.example.storeapp.model.CategoryModel
import com.example.storeapp.model.ItemsModel
import com.example.storeapp.model.SliderModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class FirebaseStoreAppRepository : StoreAppRepository {

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    override suspend fun loadBanner(): List<SliderModel> {
        val ref = firebaseDatabase.getReference("Banner")
        val snapshot = ref.get().await()
        val banners = snapshot.children.mapNotNull { it.getValue(SliderModel::class.java) }
        Log.d("FirebaseStoreAppRepository", "Loaded banners: $banners")
        return banners
    }

    override suspend fun loadAllItems(): List<ItemsModel> {
        val ref = firebaseDatabase.getReference("Items")
        val snapshot = ref.get().await()
        val allItems = snapshot.children.mapNotNull { it.getValue(ItemsModel::class.java) }
        Log.d("FirebaseStoreAppRepository", "Loaded allItems:$allItems")
        return allItems
    }

    override suspend fun loadCategory(): List<CategoryModel> {
        val ref = firebaseDatabase.getReference("Category")
        val snapshot = ref.get().await()
        val categories = snapshot.children.mapNotNull { it.getValue(CategoryModel::class.java) }
        Log.d("FirebaseStoreAppRepository", "Loaded Categories: $categories")
        return categories
    }


}