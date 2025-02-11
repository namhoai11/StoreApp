package com.example.storeapp.data.repository

import android.util.Log
import com.example.storeapp.model.CategoryModel
import com.example.storeapp.model.ProductModel
import com.example.storeapp.model.SliderModel
import com.example.storeapp.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseFireStoreRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    /** Load danh sách banner từ Firestore */
    suspend fun loadBanner(): List<SliderModel> {
        val snapshot = firestore.collection("Banner").get().await()
        val banners = snapshot.documents.mapNotNull { it.toObject(SliderModel::class.java) }
        Log.d("FirestoreRepository", "Loaded banners: $banners")
        return banners
    }

    /** Load danh sách danh mục từ Firestore */
    suspend fun loadCategory(): List<CategoryModel> {
        val snapshot = firestore.collection("Category").get().await()
        val categories = snapshot.documents.mapNotNull { doc ->
            doc.toObject(CategoryModel::class.java)?.copy(id = doc.id.toIntOrNull() ?: 0)
        }
        Log.d("FirestoreRepository", "Loaded Categories: $categories")
        return categories
    }


//    suspend fun loadCategory(): List<CategoryModel> {
//        val ref = firestore.collection("Category")
//        val snapshot = ref.get().await()
//
//        val categories = snapshot.documents.mapNotNull { doc ->
//            val id = doc.getLong("id")?.toInt() ?: 0
//            val name = doc.getString("name") ?: ""
//            val imageUrl = doc.getString("imageUrl") ?: ""
//            val description = doc.getString("description") ?: ""
//            val hidden = doc.getBoolean("hidden") ?: false
//            val productCount = doc.getLong("productCount")?.toInt() ?: 1
//            val createdAt = doc.getTimestamp("createdAt") ?: Timestamp.now()
//            val updatedAt = doc.getTimestamp("updatedAt") ?: Timestamp.now()
//
//            CategoryModel(
//                id = id,
//                name = name,
//                imageUrl = imageUrl,
//                description = description,
//                hidden = hidden,
//                productCount = productCount,
//                createdAt = createdAt,
//                updatedAt = updatedAt
//            )
//        }
//
//        Log.d("FirestoreRepository", "Loaded Categories: $categories")
//        return categories
//    }


    /** Load danh sách sản phẩm từ Firestore */
    suspend fun loadAllProducts(): List<ProductModel> {
        val snapshot = firestore.collection("Products").get().await()
        val allProducts = snapshot.documents.mapNotNull { doc ->
            doc.toObject(ProductModel::class.java)?.copy(id = doc.id)
        }
        Log.d("FirestoreRepository", "Loaded Products: $allProducts")
        return allProducts
    }

    /** Load thông tin người dùng hiện tại từ Firestore */
    suspend fun getCurrentUser(): UserModel? {
        val currentUser = auth.currentUser ?: return null
        val snapshot = firestore.collection("Users").document(currentUser.uid).get().await()

        return if (snapshot.exists()) {
            snapshot.toObject(UserModel::class.java)?.copy(id = currentUser.uid)
        } else {
            null
        }
    }


    suspend fun addWishListItem(productId: String) {
        val currentUser = auth.currentUser ?: return
        val userRef = firestore.collection("Users").document(currentUser.uid)

        userRef.update("wishList", FieldValue.arrayUnion(productId))  // Thêm productId vào danh sách
            .addOnSuccessListener {
                Log.d("FirestoreRepository", "Product added to wishlist successfully!")
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreRepository", "Error adding product to wishlist", e)
            }
    }
    suspend fun removeWishListItem(productId: String){
        val currentUser = auth.currentUser ?: return
        val userRef = firestore.collection("Users").document(currentUser.uid)
        userRef.update("wishList", FieldValue.arrayRemove(productId))  // Thêm productId vào danh sách
            .addOnSuccessListener {
                Log.d("FirestoreRepository", "Product remove to wishlist successfully!")
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreRepository", "Error remove product to wishlist", e)
            }
    }

}