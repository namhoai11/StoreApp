package com.example.storeapp.data.repository

import android.util.Log
import com.example.storeapp.model.CategoryModel
import com.example.storeapp.model.ProductModel
import com.example.storeapp.model.SliderModel
import com.google.firebase.Timestamp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import kotlinx.coroutines.tasks.await
import java.util.Date


class RealtimeDatabaseRepository {

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    suspend fun loadBanner(): List<SliderModel> {
        val ref = firebaseDatabase.getReference("Banner")
        val snapshot = ref.get().await()
        val banners = snapshot.children.mapNotNull { it.getValue(SliderModel::class.java) }
        Log.d("FirebaseStoreAppRepository", "Loaded banners: $banners")
        return banners
    }

    //    override suspend fun loadCategory(): List<CategoryModel> {
//        val ref = firebaseDatabase.getReference("Category")
//        val snapshot = ref.get().await()
//        val categories = snapshot.children.mapNotNull { it.getValue(CategoryModel::class.java) }
//        Log.d("FirebaseStoreAppRepository", "Loaded Categories: $categories")
//        return categories
//    }
    suspend fun loadCategory(): List<CategoryModel> {
        val ref = firebaseDatabase.getReference("Category")
        val snapshot = ref.get().await()

        val categories = snapshot.children.mapNotNull { dataSnapshot ->
            val id = dataSnapshot.child("id").getValue(Int::class.java) ?: 0
            val name = dataSnapshot.child("name").getValue(String::class.java) ?: ""
            val imageUrl = dataSnapshot.child("imageUrl").getValue(String::class.java) ?: ""
            val description = dataSnapshot.child("description").getValue(String::class.java) ?: ""
            val hidden = dataSnapshot.child("hidden").getValue(Boolean::class.java) ?: false
            val productCount = dataSnapshot.child("productCount").getValue(Int::class.java) ?: 1
            val createdAtLong = dataSnapshot.child("createdAt").getValue(Long::class.java) ?: 0L
            val updatedAtLong = dataSnapshot.child("updatedAt").getValue(Long::class.java) ?: 0L

            // Chuyển Long thành Date và sau đó tạo Timestamp
            val createdAt = Timestamp(Date(createdAtLong))
            val updatedAt = Timestamp(Date(updatedAtLong))

            // Tạo đối tượng CategoryModel
            CategoryModel(
                id = id,
                name = name,
                imageUrl = imageUrl,
                description = description,
                hidden = hidden,
                productCount = productCount,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }

        Log.d("FirebaseStoreAppRepository", "Loaded Categories: $categories")
        return categories
    }


    //    override suspend fun loadAllItems(): List<ProductModel> {
//        val ref = firebaseDatabase.getReference("Products")
//        val snapshot = ref.get().await()
//        val allItems = snapshot.children.mapNotNull { it.getValue(ProductModel::class.java) }
//        Log.d("FirebaseStoreAppRepository", "Loaded allItems:$allItems")
//        return allItems
//    }
    suspend fun loadAllProducts(): List<ProductModel> {
        val ref = firebaseDatabase.getReference("Products")
        val snapshot = ref.get().await()
        val allProducts = snapshot.children.mapNotNull { dataSnapshot ->
            val id = dataSnapshot.child("id").getValue(String::class.java) ?: ""
            val name = dataSnapshot.child("name").getValue(String::class.java) ?: ""
            val images = dataSnapshot.child("images")
                .getValue(object : GenericTypeIndicator<MutableList<String>>() {})
                ?: mutableListOf()
            val price = dataSnapshot.child("price").getValue(Int::class.java)?.toDouble() ?: 0.0
            val stockQuantity = dataSnapshot.child("stockQuantity").getValue(Int::class.java) ?: 0
            val categoryId = dataSnapshot.child("categoryId").getValue(String::class.java) ?: ""
            val hidden = dataSnapshot.child("hidden").getValue(Boolean::class.java) ?: false
            val showRecommended =
                dataSnapshot.child("showRecommended").getValue(Boolean::class.java) ?: false
            val description = dataSnapshot.child("description").getValue(String::class.java) ?: ""
            val rating = dataSnapshot.child("rating").getValue(Double::class.java) ?: 0.0
            val options = dataSnapshot.child("options")
                .getValue(object : GenericTypeIndicator<MutableList<String>>() {})
                ?: mutableListOf()
            val createdAtLong = dataSnapshot.child("createdAt").getValue(Long::class.java) ?: 0L
            val updatedAtLong = dataSnapshot.child("updatedAt").getValue(Long::class.java) ?: 0L

            // Chuyển Long thành Date và sau đó tạo Timestamp
            val createdAt = Timestamp(Date(createdAtLong))
            val updatedAt = Timestamp(Date(updatedAtLong))

            ProductModel(
                id = id,
                name = name,
                images = images,
                price = price,
                stockQuantity = stockQuantity,
                categoryId = categoryId,
                hidden = hidden,
                showRecommended = showRecommended,
                description = description,
                rating = rating,
                options = options,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
        Log.d("FirebaseStoreAppRepository", "Loaded Categories: $allProducts")
        return allProducts
    }
}