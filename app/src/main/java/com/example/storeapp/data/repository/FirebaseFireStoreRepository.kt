package com.example.storeapp.data.repository

import android.util.Log
import com.example.storeapp.model.CartModel
import com.example.storeapp.model.CategoryModel
import com.example.storeapp.model.CouponModel
import com.example.storeapp.model.ProductModel
import com.example.storeapp.model.ProductsOnCart
import com.example.storeapp.model.SliderModel
import com.example.storeapp.model.UserLocationModel
import com.example.storeapp.model.UserModel
import com.example.storeapp.ui.screen.cart.CartAction
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

    suspend fun getProductByListId(productIds: List<String>): List<ProductModel> {
        if (productIds.isEmpty()) return emptyList()

        return firestore.collection("Products")
            .whereIn("id", productIds)
            .get()
            .await()
            .documents
            .mapNotNull { it.toObject(ProductModel::class.java) }
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

    suspend fun addAddressToFireStore(
        userLocationModel: UserLocationModel
    ): Result<Unit> {
        val userLocationRef = firestore.collection("UserLocation")

        return try {
            // Thêm dữ liệu vào collection, sử dụng ID từ model (hoặc generate tự động nếu cần)
            val newUserLocationRef = userLocationRef.document()
            val newUserLocation = UserLocationModel(
                id = newUserLocationRef.id,
                userName = userLocationModel.userName,
                street = userLocationModel.street,
                province = userLocationModel.province,
                district = userLocationModel.district,
                ward = userLocationModel.ward,
                userId = userLocationModel.userId,
                provinceId = userLocationModel.provinceId,
                districtId = userLocationModel.districtId,
                wardId = userLocationModel.wardId,
//                latitude = userLocationModel.latitude,
//                longitude = userLocationModel.longitude,
//                createdAt = userLocationModel.createdAt,
//                updatedAt = userLocationModel.updatedAt
            )
            newUserLocationRef.set(newUserLocation).await()
            // Thành công
            Log.d("Firestore", "User location added successfully")
            Result.success(Unit) // Trả về kết quả thành công
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error adding Location", e)
            Result.failure(e) // Trả về lỗi
        }
    }

    suspend fun getListAddressByUser(userId: String): Result<List<UserLocationModel>> {
        val userLocationRef = firestore.collection("UserLocation")

        return try {
            val snapshot = userLocationRef.whereEqualTo("userId", userId).get().await()
            val addressList =
                snapshot.documents.mapNotNull { it.toObject(UserLocationModel::class.java) }

            Log.d("FirestoreRepository", "Fetched ${addressList.size} addresses for user $userId")
            Result.success(addressList)
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error fetching user addresses", e)
            Result.failure(e)
        }
    }

    suspend fun updateDefaultLocation(userId: String, newDefaultLocationId: String): Result<Unit> {
        return try {
            val userRef = firestore.collection("Users").document(userId)

            // Cập nhật trường defaultLocationId
            userRef.update("defaultLocationId", newDefaultLocationId).await()
//            userRef.set(mapOf("defaultLocationId" to newDefaultLocationId), SetOptions.merge()).await()
            Log.d("FirestoreRepository", "Updated defaultLocationId successfully")
            Result.success(Unit) // Trả về kết quả thành công
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error updating defaultLocationId", e)
            Result.failure(e) // Trả về lỗi
        }
    }

    suspend fun deleteAddressById(addressId: String): Result<Unit> {
        val userLocationRef = firestore.collection("UserLocation").document(addressId)
        return try {
            userLocationRef.delete().await()
            Log.d("Firestore", "User location deleted successfully: $addressId")
            Result.success(Unit) // Xóa thành công
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error deleting location: $addressId", e)
            Result.failure(e) // Trả về lỗi
        }
    }


    suspend fun getCartByUser(userId: String): CartModel? {
        return try {
            val querySnapshot = firestore.collection("Cart")
                .whereEqualTo("userId", userId) // Lọc theo userId
                .limit(1) // Chỉ lấy 1 kết quả
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val cart = querySnapshot.documents.first().toObject(CartModel::class.java)
                Log.d("FirestoreRepository", "Loaded Cart for userID $userId: $cart")
                cart
            } else {
                Log.w("FirestoreRepository", "Cart not found for userID: $userId")
                null
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error loading cart for userID: $userId", e)
            null
        }
    }


//    suspend fun increaseProductQuantity(
//        currentUserId: String, productOnCart: ProductsOnCart
//    ): Result<Unit> {
//        return updateProductQuantity(currentUserId, productOnCart, isIncreasing = true)
//    }
//
//    suspend fun decreaseProductQuantity(
//        currentUserId: String, productOnCart: ProductsOnCart
//    ): Result<Unit> {
//        return updateProductQuantity(currentUserId, productOnCart, isIncreasing = false)
//    }

    suspend fun updateProductQuantityInCart(
        currentUserId: String,
        productOnCart: ProductsOnCart,
        action: CartAction
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

                val existingProductIndex = updatedListProducts.indexOfFirst {
                    it.productId == productOnCart.productId &&
                            it.productOptions == productOnCart.productOptions &&
                            it.colorOptions == productOnCart.colorOptions
                }

                if (existingProductIndex != -1) {
                    when (action) {
                        is CartAction.Increase -> {
                            val existingProduct = updatedListProducts[existingProductIndex]
                            updatedListProducts[existingProductIndex] =
                                existingProduct.copy(quantity = existingProduct.quantity + 1)
                        }

                        is CartAction.Decrease -> {
                            val existingProduct = updatedListProducts[existingProductIndex]
                            if (existingProduct.quantity > 1) {
                                updatedListProducts[existingProductIndex] =
                                    existingProduct.copy(quantity = existingProduct.quantity - 1)
                            } else {
                                updatedListProducts.removeAt(existingProductIndex)
                            }
                        }

                        is CartAction.Remove -> {
                            updatedListProducts.removeAt(existingProductIndex)
                        }
                    }
                }
                cartId = cartDoc.id
            }

            val newCartRef = cartId?.let { cartRef.document(it) } ?: cartRef.document()
            val newCart = CartModel(
                id = newCartRef.id,
                userId = currentUserId,
                products = updatedListProducts
            )

            newCartRef.set(newCart, SetOptions.merge()).await()
            Log.d("FirestoreRepository", "Cart updated successfully with ID: ${newCartRef.id}")

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error updating product in cart", e)
            Result.failure(e)
        }
    }


//    fun updateCartWhenProductChanges(productId: String) {
//        firestore.collection("Cart")
//            .whereArrayContains("items.productId", productId) // Tìm giỏ hàng chứa sản phẩm
//            .get()
//            .addOnSuccessListener { querySnapshot ->
//                for (document in querySnapshot.documents) {
//                    val cart = document.toObject(CartModel::class.java)
//                    cart?.let {
//                        // Cập nhật giá mới từ Product vào Cart
////                        val updatedItems = it.items.map { item ->
////                            if (item.productId == productId) {
//////                                item.copy(price = getProductPrice(productId)) // Lấy giá mới
////                            } else item
////                        }
//                        firestore.collection("Cart").document(document.id)
////                            .update("items", updatedItems)
//                    }
//                }
//            }
//    }


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

    fun observeAllProducts(): Flow<List<ProductModel>> = callbackFlow {
        val productsRef = firestore.collection("Products")
        val listener = productsRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error) // Đóng Flow nếu có lỗi
                return@addSnapshotListener
            }

            val productList =
                snapshot?.documents?.mapNotNull { it.toObject(ProductModel::class.java) }
                    ?: emptyList()
            Log.d("Firestore", "Products updated: $productList")
            trySend(productList).isSuccess
        }

        awaitClose { listener.remove() } // Hủy lắng nghe khi Flow bị đóng
    }

    fun observeProductsByListId(productIds: List<String>): Flow<List<ProductModel>> = callbackFlow {
        if (productIds.isEmpty()) {
            trySend(emptyList()).isSuccess
            close()
            return@callbackFlow
        }

        val productsRef = firestore.collection("Products").whereIn("id", productIds)

        val listener = productsRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error) // Đóng Flow nếu có lỗi
                return@addSnapshotListener
            }

            val productList =
                snapshot?.documents?.mapNotNull { it.toObject(ProductModel::class.java) }
                    ?: emptyList()
            Log.d("Firestore", "Products in cart updated: $productList")
            trySend(productList).isSuccess
        }

        awaitClose { listener.remove() } // Hủy lắng nghe khi Flow bị đóng
    }

    suspend fun addCouponToFireStore(coupon: CouponModel): Result<Unit> {
        val couponRef = firestore.collection("Coupons") // Collection lưu coupon

        return try {
            // Tạo document mới với ID tự động
            val newCouponRef = couponRef.document()

            // Cập nhật model với ID mới
            val newCoupon = coupon.copy(
                id = newCouponRef.id,
                code = newCouponRef.id
            )

            // Lưu coupon vào Firestore
            newCouponRef.set(newCoupon).await()

            Log.d("Firestore", "Coupon added successfully")
            Result.success(Unit) // Thành công
        } catch (e: Exception) {
            Log.e("Firestore", "Error adding Coupon", e)
            Result.failure(e) // Trả về lỗi
        }
    }

    suspend fun addOrUpdateCouponToFireStore(coupon: CouponModel): Result<Unit> {
        val couponRef = firestore.collection("Coupons") // Collection lưu coupon

        return try {
            val couponDocumentRef = if (coupon.id.isNotBlank() && coupon.code.isNotBlank()) {
                couponRef.document(coupon.id)
            } else {
                couponRef.document()
            }

            val updatedCoupon = coupon.copy(
                id = couponDocumentRef.id,
                code = coupon.code.ifBlank { couponDocumentRef.id }
            )

            // Dùng set với merge để không ghi đè toàn bộ nếu update
            couponDocumentRef.set(updatedCoupon, SetOptions.merge()).await()

            Log.d("Firestore", "Coupon added/updated successfully")
            Result.success(Unit) // Thành công
        } catch (e: Exception) {
            Log.e("Firestore", "Error adding/updating Coupon", e)
            Result.failure(e) // Trả về lỗi
        }
    }


    suspend fun getCoupons(): Result<List<CouponModel>> {
        return try {
            val snapshot = firestore.collection("Coupons").get().await()
            val coupons = snapshot.documents.mapNotNull { it.toObject(CouponModel::class.java) }
            Log.d("Firestore", "Confirm loading Coupons")

            Result.success(coupons)
        } catch (e: Exception) {
            Log.e("Firestore", "Error loading Coupons", e)
            Result.failure(e)
        }
    }

    suspend fun getCouponById(couponId: String): Result<CouponModel> {
        return try {
            val doc = firestore.collection("Coupons").document(couponId).get().await()
            val coupon = doc.toObject(CouponModel::class.java)
            if (coupon != null) {
                Result.success(coupon)
            } else {
                Result.failure(Exception("Không tìm thấy coupon"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeCouponById(couponId: String): Result<Unit> {
        val couponRef = firestore.collection("Coupons").document(couponId)
        return try {
            couponRef.delete().await()
            Log.d("FireStore", "coupon delete successfully: $couponId")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FireStore", "coupon delete fail: $e")

            Result.failure(e)
        }
    }


}