package com.example.storeapp.data.migration

import android.util.Log
import com.example.storeapp.model.AvailableOptions
import com.example.storeapp.model.CategoryModel
import com.example.storeapp.model.ColorOptions
import com.example.storeapp.model.ProductModel
import com.example.storeapp.model.ProductOptions
import com.example.storeapp.model.SliderModel
import com.example.storeapp.model.StockByVariant
import com.google.firebase.Timestamp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

suspend fun migrateDataFromRealtimeToFirestore() {
    val database = FirebaseDatabase.getInstance().getReference("Products")
    val firestore = FirebaseFirestore.getInstance()
    val productsCollection = firestore.collection("Products")

    database.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {

            for (productSnapshot in snapshot.children) {
                val productMap = productSnapshot.value as? Map<String, Any> ?: continue

                val stockByVariantList =
                    (productMap["stockByVariant"] as? List<Map<String, Any>>)?.map {
                        StockByVariant(
                            colorName = it["colorName"] as? String ?: "",
                            optionName = it["optionName"] as? String ?: "",
                            quantity = (it["quantity"] as? Number)?.toInt() ?: 0
                        )
                    } ?: emptyList()

                val availableOptionsMap = productMap["availableOptions"] as? Map<String, Any>
                val listProductOptions =
                    (availableOptionsMap?.get("listProductOptions") as? List<Map<String, Any>>)?.map {
                        ProductOptions(
                            optionsName = it["optionName"] as? String ?: "",
                            priceForOptions = (it["price"] as? Number)?.toDouble() ?: 0.0
                        )
                    } ?: emptyList()

                val listColorOptions =
                    (availableOptionsMap?.get("listColorOptions") as? List<Map<String, Any>>)?.map {
                        ColorOptions(
                            colorName = it["color"] as? String ?: "",
                            imagesColor = it["image"] as? String ?: ""
                        )
                    } ?: emptyList()

                val product = ProductModel(
                    id = productMap["id"] as? String ?: "",
                    name = productMap["name"] as? String ?: "",
                    images = productMap["images"] as? List<String> ?: emptyList(),
                    price = (productMap["price"] as? Number)?.toDouble() ?: 0.0,
                    stockQuantity = (productMap["stockQuantity"] as? Number)?.toInt() ?: 0,
                    categoryId = productMap["categoryId"] as? String ?: "",
                    hidden = productMap["hidden"] as? Boolean ?: false,
                    showRecommended = productMap["showRecommended"] as? Boolean ?: false,
                    description = productMap["description"] as? String ?: "",
                    rating = (productMap["rating"] as? Number)?.toDouble() ?: 0.0,
                    options = productMap["options"] as? List<String> ?: emptyList(),
                    stockByVariant = stockByVariantList,
                    availableOptions = AvailableOptions(
                        listProductOptions = listProductOptions,
                        listColorOptions = listColorOptions
                    ),
                    createdAt = convertToTimestamp(productMap["createdAt"]),
                    updatedAt = convertToTimestamp(productMap["updatedAt"])
                )
                // Lưu vào Firestore
                productsCollection.document(product.id)
                    .set(product)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Product ${product.id} migrated successfully!")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Error migrating product ${product.id}", e)
                    }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("RealtimeDB", "Failed to read data", error.toException())
        }
    })
}

suspend fun migrateCategoriesFromRealtimeToFirestore() {
    val database = FirebaseDatabase.getInstance().getReference("Category")
    val firestore = FirebaseFirestore.getInstance()
    val categoriesCollection = firestore.collection("Category")

    database.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            for (categorySnapshot in snapshot.children) {
                val categoryMap = categorySnapshot.value as? Map<*, *> ?: continue

                // Kiểm tra dữ liệu trong categoryMap
                Log.d("RealtimeDB", "Category data: $categoryMap")
                val category = CategoryModel(
                    id = categoryMap["id"] as? Int ?: 0,
                    name = categoryMap["name"] as? String ?: "",
                    imageUrl = categoryMap["imageUrl"] as? String ?: "",
                    description = categoryMap["description"] as? String ?: "",
                    hidden = categoryMap["hidden"] as? Boolean ?: false,
                    productCount = (categoryMap["productCount"] as? Number)?.toInt() ?: 1,
                    createdAt = convertToTimestamp(categoryMap["createdAt"]),
                    updatedAt = convertToTimestamp(categoryMap["updatedAt"])
                )

                // Lưu vào Firestore
                val categoryId = categorySnapshot.key ?: category.id.toString()
                categoriesCollection.document(categoryId)
                    .set(category)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Category $categoryId migrated successfully!")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Error migrating category $categoryId", e)
                    }

            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("RealtimeDB", "Failed to read category data", error.toException())
        }
    })
}

suspend fun migrateSlidersFromRealtimeToFirestore() {
    val database = FirebaseDatabase.getInstance().getReference("Banner")
    val firestore = FirebaseFirestore.getInstance()
    val slidersCollection = firestore.collection("Banner")

    database.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            for (sliderSnapshot in snapshot.children) {
                val sliderMap = sliderSnapshot.value as? Map<String, Any> ?: continue
                val slider = SliderModel(
                    url = sliderMap["url"] as? String ?: ""
                )

                // Lưu vào Firestore
                slidersCollection.document() // Firestore sẽ tự động tạo ID mới cho mỗi document
                    .set(slider)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Slider migrated successfully!")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Error migrating slider", e)
                    }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("RealtimeDB", "Failed to read slider data", error.toException())
        }
    })
}


fun convertToTimestamp(value: Any?): Timestamp {
    return when (value) {
        is Long -> Timestamp(
            value / 1000,
            ((value % 1000) * 1000000).toInt()
        ) // Chuyển từ milliseconds → seconds + nanoseconds
        is Double -> Timestamp(value.toLong() / 1000, ((value.toLong() % 1000) * 1000000).toInt())
        is Timestamp -> value
        else -> Timestamp.now() // Nếu dữ liệu sai, dùng timestamp hiện tại
    }
}

