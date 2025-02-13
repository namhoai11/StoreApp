package com.example.storeapp.data.repository

import android.util.Log
import com.example.storeapp.model.CartModel
import com.example.storeapp.model.CategoryModel
import com.example.storeapp.model.ProductModel
import com.example.storeapp.model.ProductsOnCart
import com.example.storeapp.model.SliderModel
import com.example.storeapp.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

    suspend fun getProductById(productId: String): ProductModel? {
        val documentSnapshot = firestore.collection("Products").document(productId).get().await()
        val product = documentSnapshot.toObject(ProductModel::class.java)

        Log.d("FirestoreRepository", "Loaded Product with ID $productId: $product")

        return product
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


    suspend fun addWishListItem(currentUserId: String, productId: String) {
        val userRef = firestore.collection("Users").document(currentUserId)

//        userRef.update("wishList", FieldValue.arrayUnion(productId))  // Thêm productId vào danh sách
//            .addOnSuccessListener {
//                Log.d("FirestoreRepository", "Product added to wishlist successfully!")
//            }
//            .addOnFailureListener { e ->
//                Log.e("FirestoreRepository", "Error adding product to wishlist", e)
//            }

        try {
            userRef.update("wishList", FieldValue.arrayUnion(productId)).await()
            Log.d("FirestoreRepository", "Product added to wishlist successfully!")
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error adding product to wishlist", e)
        }
    }


    suspend fun removeWishListItem(currentUserId: String, productId: String) {
        val userRef = firestore.collection("Users").document(currentUserId)
        try {
            userRef.update("wishList", FieldValue.arrayRemove(productId)).await()
            Log.d("FirestoreRepository", "Product added to wishlist successfully!")
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error adding product to wishlist", e)
        }
    }

    suspend fun addProductToCart(productOnCart: ProductsOnCart) {
        val currentUser = getCurrentUser() ?: return
        val cartRef = firestore.collection("Cart")
        try {
            val snapshot = cartRef.whereEqualTo("userId", currentUser.id).get().await()

            if (!snapshot.isEmpty) {
                val cartDoc = snapshot.documents.first()
                val cart = cartDoc.toObject(CartModel::class.java)
                val updatedListProducts = cart?.products?.toMutableList() ?: mutableListOf()
                val existingProduct =
                    updatedListProducts.find { it.productId == productOnCart.productId }
                if (existingProduct != null) {
                    updatedListProducts[updatedListProducts.indexOf(existingProduct)] =
                        existingProduct.copy(quantity = existingProduct.quantity + 1)
                } else {
                    updatedListProducts.add(productOnCart)
                }
//                val totalPrice = updatedListProducts.sumOf { it.productPrice * it.quantity }
                val updateData = mapOf(
//                    "products" to updatedListProducts, "total" to totalPrice
                    "products" to updatedListProducts,

                )
                cartDoc.reference.update(updateData).await()
                Log.d("FirestoreRepository", "Updated cart ${cartDoc.id} with new product")
            } else {
                val newCart = CartModel(
                    id = "", // Firestore sẽ tự động cấp ID
                    userId = currentUser.id,
                    products = listOf(productOnCart),
//                    total = productOnCart.productPrice * productOnCart.quantity
                )
                val newCartRef = cartRef.add(newCart).await()
                newCartRef.update("id", newCartRef.id)  // Cập nhật lại ID sau khi tạo
                Log.d("FirestoreRepository", "Created new cart with ID: ${newCartRef.id}")
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Created new cart erroe: $e")
        }
    }

    suspend fun addProductToCartUseSet(
        currentUserId: String, productOnCart: ProductsOnCart
    ): Result<Unit> {
        val cartRef = firestore.collection("Cart")
        return try {
            val snapshot = cartRef.whereEqualTo("userId", currentUserId).get().await()

            val updatedListProducts = mutableListOf<ProductsOnCart>()
            var cartId: String? = null

            if (!snapshot.isEmpty) {
                val cartDoc = snapshot.documents.first()
                val cart = cartDoc.toObject(CartModel::class.java)
                updatedListProducts.addAll(cart?.products ?: emptyList())

                val existingProduct =
                    updatedListProducts.find {
                        it.productId == productOnCart.productId &&
                                it.productOptions == productOnCart.productOptions &&
                                it.colorOptions == productOnCart.colorOptions
                    }

                if (existingProduct != null) {
                    updatedListProducts[updatedListProducts.indexOf(existingProduct)] =
                        existingProduct.copy(quantity = existingProduct.quantity + 1)
                } else {
                    updatedListProducts.add(productOnCart)
                }
                cartId = cartDoc.id
            } else {
                updatedListProducts.add(productOnCart)
            }

//            val totalPrice = updatedListProducts.sumOf { it.productPrice * it.quantity }

            val newCartRef = cartId?.let { cartRef.document(it) } ?: cartRef.document()
            val newCart = CartModel(
                id = newCartRef.id,
                userId = currentUserId,
                products = updatedListProducts,
//                total = totalPrice
            )

            newCartRef.set(newCart, SetOptions.merge()).await()
            Log.d("FirestoreRepository", "Cart updated successfully with ID: ${newCartRef.id}")

            Result.success(Unit) // Trả về kết quả thành công
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error adding product to cart", e)
            Result.failure(e) // Trả về lỗi
        }
    }


    fun observeProductById(productId: String): Flow<ProductModel?> = callbackFlow {
        val productRef = firestore.collection("Products").document(productId)

        val listener = productRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error) // Đóng Flow nếu có lỗi
                return@addSnapshotListener
            }
            val product = snapshot?.toObject(ProductModel::class.java)
            Log.d("Firestore", "Product updated: $product")
            trySend(product).isSuccess
        }
        awaitClose { listener.remove() } // Hủy lắng nghe khi Flow bị đóng
    }
}