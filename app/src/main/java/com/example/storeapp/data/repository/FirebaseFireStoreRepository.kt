package com.example.storeapp.data.repository

import android.net.Uri
import android.util.Log
import com.example.storeapp.model.CartModel
import com.example.storeapp.model.CategoryModel
import com.example.storeapp.model.CouponModel
import com.example.storeapp.model.OrderModel
import com.example.storeapp.model.OrderStatus
import com.example.storeapp.model.ProductModel
import com.example.storeapp.model.ProductsOnCart
import com.example.storeapp.model.SliderModel
import com.example.storeapp.model.UserLocationModel
import com.example.storeapp.model.UserModel
import com.example.storeapp.ui.screen.cart.CartAction
import com.example.storeapp.ui.screen.cart.ProductsOnCartToShow
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.net.URLDecoder

class FirebaseFireStoreRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance().reference

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
            doc.toObject(CategoryModel::class.java)?.copy(id = doc.id)
        }
        Log.d("FirestoreRepository", "Loaded Categories: $categories")
        return categories
    }

    suspend fun getCategoryById(categoryId: String): Result<CategoryModel> {
        return try {
            val doc = firestore.collection("Category").document(categoryId).get().await()
            val category = doc.toObject(CategoryModel::class.java)
            if (category != null) {
                Result.success(category)
            } else {
                Result.failure(Exception("Không tìm thấy danh muc"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addOrUpdateCategoryToFireStore(category: CategoryModel): Result<Unit> {
        val categoryRef = firestore.collection("Category")
        return try {
            val categoryDocumentRef = if (category.id.isNotBlank()) {
                categoryRef.document(category.id)
            } else {
                categoryRef.document()
            }

            val updatedCategory = category.copy(
                id = categoryDocumentRef.id,
                updatedAt = Timestamp.now()
            )

            categoryDocumentRef.set(updatedCategory, SetOptions.merge()).await()

            Log.d("Firestore", "Category added/updated successfully")
            Result.success(Unit) // Không cần trả về id
        } catch (e: Exception) {
            Log.e("Firestore", "Error adding/updating Category", e)
            Result.failure(e)
        }
    }

    suspend fun removeCategorytById(categoryId: String): Result<Unit> {
        val categoryRef = firestore.collection("Category").document(categoryId)
        return try {
            val snapshot = categoryRef.get().await()
            val category = snapshot.toObject(CategoryModel::class.java) ?: return Result.failure(
                Exception("Category not found")
            )


            val imageUrl = category.imageUrl
            Log.d("FireStore", "Image URLs to delete: $imageUrl")


            // Xóa ảnh (nếu có)
            if (imageUrl.isNotBlank()) {
                deleteImageFromStorage(imageUrl).onFailure {
                    Log.e("FireStore", "Failed to delete image: $imageUrl", it)
                }
            }
            // Xóa danh mục
            categoryRef.delete().await()
            Log.d("FireStore", "Category deleted successfully: $categoryId")

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FireStore", "Category delete failed", e)
            Result.failure(e)
        }
    }


//    /** Load danh sách sản phẩm từ Firestore */
//    suspend fun loadAllProducts(): List<ProductModel> {
//        val snapshot = firestore.collection("Products").get().await()
//        val allProducts = snapshot.documents.mapNotNull { doc ->
//            doc.toObject(ProductModel::class.java)?.copy(id = doc.id)
//        }
//        Log.d("FirestoreRepository", "Loaded Products: $allProducts")
//        return allProducts
//    }

    suspend fun loadAllProducts(): List<ProductModel> {
        return try {
            val snapshot = firestore.collection("Products").get().await()

            // In ra dữ liệu raw từ Firestore
            snapshot.documents.forEach { doc ->
                Log.d("FirestoreRepository", "Document ID: ${doc.id}, Data: ${doc.data}")
            }

            val allProducts = snapshot.documents.mapNotNull { doc ->
                doc.toObject(ProductModel::class.java)?.copy(id = doc.id)
            }

            Log.d("FirestoreRepository", "Loaded Products: $allProducts")
            allProducts
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error loading products", e)
            emptyList()
        }
    }


    suspend fun getProductById(productId: String): Result<ProductModel?> {
        return try {
            val documentSnapshot =
                firestore.collection("Products").document(productId).get().await()
            val product = documentSnapshot.toObject(ProductModel::class.java)

            Log.d("FirestoreRepository", "Loaded Product with ID $productId: $product")
            Result.success(product) // Thành công, trả về kết quả

        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error loading product with ID $productId", e)
            Result.failure(e) // Lỗi, trả về exception
        }
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

    suspend fun updateProductQuantityForCheckout(
        productOnCart: ProductsOnCartToShow
    ): Result<Unit> {
        return try {
            val productRef = firestore.collection("Products").document(productOnCart.productId)
            val snapshot = productRef.get().await()

            if (!snapshot.exists()) {
                return Result.failure(Exception("Sản phẩm không tồn tại"))
            }

            val product = snapshot.toObject(ProductModel::class.java)
                ?: return Result.failure(Exception("Lỗi chuyển đổi dữ liệu sản phẩm"))

            // Cập nhật stockByVariant
            val updatedStockByVariant = product.stockByVariant.map {
                if (it.colorName == productOnCart.colorOptions && it.optionName == productOnCart.productOptions) {
                    it.copy(quantity = (it.quantity - productOnCart.quantity).coerceAtLeast(0)) // Không cho xuống dưới 0
                } else {
                    it
                }
            }
            // Cập nhật tổng stockQuantity
            val updatedTotalStock = updatedStockByVariant.sumOf { it.quantity }
            // Tạo đối tượng cập nhật
            val updatedProduct = product.copy(
                stockByVariant = updatedStockByVariant,
                stockQuantity = updatedTotalStock
            )
            // Cập nhật dữ liệu vào Firestore
            productRef.set(updatedProduct, SetOptions.merge()).await()

            Log.d("FirestoreRepository", "Cập nhật số lượng sản phẩm thành công bằng set()")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Lỗi khi cập nhật số lượng sản phẩm", e)
            Result.failure(e)
        }
    }


    suspend fun addOrUpdateProductToFireStore(product: ProductModel): Result<Unit> {
        val productRef = firestore.collection("Products")

        return try {
            val productDocumentRef = if (product.id.isNotBlank()) {
                productRef.document(product.id)
            } else {
                productRef.document()
            }

            val updatedProduct = product.copy(
                id = productDocumentRef.id,
                updatedAt = Timestamp.now()
            )

            productDocumentRef.set(updatedProduct, SetOptions.merge()).await()

            Log.d("Firestore", "Product added/updated successfully")
            Result.success(Unit) // Không cần trả về id
        } catch (e: Exception) {
            Log.e("Firestore", "Error adding/updating Product", e)
            Result.failure(e)
        }
    }

    suspend fun uploadImageToStorage(imageUri: Uri, path: String): Result<String> {
        return try {
            val imageRef = storage.child(path)
            imageRef.putFile(imageUri).await()
            val downloadUrl =
                imageRef.downloadUrl.await().toString() // Lấy URL sau khi upload thành công
            Result.success(downloadUrl)
        } catch (e: Exception) {
            Log.e("FirebaseStorage", "Upload image failed", e)
            Result.failure(e)
        }
    }

    private suspend fun deleteImageFromStorage(imageUrl: String): Result<Unit> {
        return try {
            if (!imageUrl.startsWith("https://")) {
                return Result.failure(Exception("Invalid image URL format"))
            }

            val decodedUrl = withContext(Dispatchers.IO) {
                URLDecoder.decode(imageUrl, "UTF-8")
            }

            val pathStartIndex = decodedUrl.indexOf("/o/") + 3
            val pathEndIndex = decodedUrl.indexOf("?alt=")

            if (pathStartIndex == -1 || pathEndIndex == -1) {
                return Result.failure(Exception("Invalid image URL"))
            }

            val imagePath = decodedUrl.substring(pathStartIndex, pathEndIndex)
            val imageRef = storage.child(imagePath)

            imageRef.delete().await() // Xóa ảnh trên Firebase Storage
            Log.d("FirebaseStorage", "Deleted image successfully: $imageUrl")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FirebaseStorage", "Error deleting image", e)
            Result.failure(e)
        }
    }


    suspend fun removeProductById(productId: String): Result<Unit> {
        val productRef = firestore.collection("Products").document(productId)

        return try {
            val snapshot = productRef.get().await()
            val product = snapshot.toObject(ProductModel::class.java) ?: return Result.failure(
                Exception("Product not found")
            )

            val colorImageUrls = product.availableOptions?.listColorOptions
                ?.mapNotNull { it.imageColorUrl.takeIf { url -> url.isNotBlank() } }
                ?: emptyList()

            val imageUrls = product.images + colorImageUrls
            Log.d("FireStore", "Image URLs to delete: $imageUrls")

            // Xóa tất cả ảnh song song
            val deleteImageJobs = imageUrls.map { imageUrl ->
                CoroutineScope(Dispatchers.IO).async {
                    deleteImageFromStorage(imageUrl).onFailure {
                        Log.e("FireStore", "Failed to delete image: $imageUrl", it)
                    }
                }
            }

            // Chờ tất cả ảnh được xóa xong
            deleteImageJobs.awaitAll()

            // Xóa sản phẩm sau khi đã xóa hết ảnh
            productRef.delete().await()
            Log.d("FireStore", "Product deleted successfully: $productId")

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FireStore", "Product delete failed", e)
            Result.failure(e)
        }
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

    suspend fun getUserById(userId: String): UserModel? {
        val snapshot = firestore.collection("Users").document(userId).get().await()

        return if (snapshot.exists()) {
            snapshot.toObject(UserModel::class.java)?.copy(id = userId)
        } else {
            null
        }
    }

    /** Cập nhật thông tin người dùng lên Firestore và trả về Result<Unit> */
    suspend fun updateUser(user: UserModel): Result<Unit> {
        return try {
            val userMap = mapOf(
                "firstName" to user.firstName,
                "lastName" to user.lastName,
                "dateOfBirth" to user.dateOfBirth,
                "gender" to user.gender,
                "phone" to user.phone,
                "updatedAt" to Timestamp.now()
            )

            firestore.collection("Users").document(user.id)
                .set(userMap, SetOptions.merge()) // Chỉ cập nhật các trường được truyền
                .await()

            Log.d("FirebaseFireStoreRepository", "Cập nhật thành công: $userMap")
            Result.success(Unit) // Trả về thành công

        } catch (e: Exception) {
            Log.e("FirebaseFireStoreRepository", "Lỗi khi cập nhật người dùng", e)
            Result.failure(e) // Trả về lỗi
        }
    }




    suspend fun addWishListItem(currentUserId: String, productId: String) {
        val userRef = firestore.collection("Users").document(currentUserId)

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

    suspend fun removeCartByUser(userId: String): Result<Unit> {
        return try {
            val querySnapshot = firestore.collection("Cart")
                .whereEqualTo("userId", userId)
                .limit(1)
                .get()
                .await() // Lấy danh sách giỏ hàng của user
            if (querySnapshot.isEmpty) {
                Log.d("FireStore", "Không tìm thấy giỏ hàng của user: $userId")
                return Result.success(Unit)
            }

            // Xóa từng tài liệu trong kết quả truy vấn
            for (document in querySnapshot.documents) {
                document.reference.delete().await()
            }

            Log.d("FireStore", "Xóa giỏ hàng thành công: $userId")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FireStore", "Lỗi khi xóa giỏ hàng: $e")
            Result.failure(e)
        }
    }


//    suspend fun getCartById(cartId: String): Result<CartModel?> {
//        return try {
//            val documentSnapshot = firestore.collection("Cart").document(cartId).get().await()
//            val cart = documentSnapshot.toObject(CartModel::class.java)
//
//            Log.d("FirestoreRepository", "Loaded Product with ID $cartId: $cart")
//            Result.success(cart) // Thành công, trả về kết quả
//        } catch (e: Exception) {
//            Log.e("FirestoreRepository", "Error loading product with ID $cartId", e)
//            Result.failure(e) // Lỗi, trả về exception
//        }
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

//    suspend fun addCouponToFireStore(coupon: CouponModel): Result<Unit> {
//        val couponRef = firestore.collection("Coupons") // Collection lưu coupon
//
//        return try {
//            // Tạo document mới với ID tự động
//            val newCouponRef = couponRef.document()
//
//            // Cập nhật model với ID mới
//            val newCoupon = coupon.copy(
//                id = newCouponRef.id,
//                code = newCouponRef.id
//            )
//
//            // Lưu coupon vào Firestore
//            newCouponRef.set(newCoupon).await()
//
//            Log.d("Firestore", "Coupon added successfully")
//            Result.success(Unit) // Thành công
//        } catch (e: Exception) {
//            Log.e("Firestore", "Error adding Coupon", e)
//            Result.failure(e) // Trả về lỗi
//        }
//    }

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

    suspend fun updateCouponQuantityForCheckout(couponId: String): Result<Unit> {
        return try {
            val couponRef = firestore.collection("Coupons").document(couponId)
            val snapshot = couponRef.get().await()

            if (!snapshot.exists()) {
                return Result.failure(Exception("Voucher không tồn tại"))
            }

            val coupon = snapshot.toObject(CouponModel::class.java)
                ?: return Result.failure(Exception("Lỗi chuyển đổi dữ liệu voucher"))

            if (coupon.quantity <= 0) {
                return Result.failure(Exception("Voucher đã hết số lượng"))
            }

            // Trừ đi 1 đơn vị khi áp dụng voucher
            val updatedCoupon = coupon.copy(quantity = coupon.quantity - 1)

            // Cập nhật lại Firestore
            couponRef.set(updatedCoupon, SetOptions.merge()).await()

            Log.d("FirestoreRepository", "Cập nhật số lượng voucher thành công")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Lỗi khi cập nhật số lượng voucher", e)
            Result.failure(e)
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

    suspend fun getActiveCoupons(): Result<List<CouponModel>> {
        return try {
            val snapshot = firestore.collection("Coupons").get().await()
            val now = Timestamp.now()

            val activeCoupons =
                snapshot.documents.mapNotNull { it.toObject(CouponModel::class.java) }
                    .filter { it.startDate <= now && it.endDate >= now && it.quantity > 0 } // Lọc chỉ lấy Coupon đang hoạt động

            Log.d("Firestore", "Đã tải ${activeCoupons.size} coupon đang hoạt động")

            Result.success(activeCoupons)
        } catch (e: Exception) {
            Log.e("Firestore", "Lỗi khi tải Coupons", e)
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

//    suspend fun addOrUpdateProduct(product: ProductModel): Result<Unit> {
//        val productRef = firestore.collection("Products") // Collection lưu sản phẩm
//
//        return try {
//            val productDocumentRef = if (product.id.isNotBlank()) {
//                productRef.document(product.id)
//            } else {
//                productRef.document()
//            }
//
//            val updatedProduct = product.copy(
//                id = productDocumentRef.id,
//                createdAt = product.createdAt.takeIf { product.id.isNotBlank() } ?: Timestamp.now(),
//                updatedAt = Timestamp.now()
//            )
//
//            // Sử dụng set với merge để cập nhật dữ liệu mà không ghi đè toàn bộ
//            productDocumentRef.set(updatedProduct, SetOptions.merge()).await()
//
//            Log.d("Firestore", "Product added/updated successfully")
//            Result.success(Unit) // Thành công
//        } catch (e: Exception) {
//            Log.e("Firestore", "Error adding/updating Product", e)
//            Result.failure(e) // Trả về lỗi
//        }
//    }

    suspend fun addOrderToFirestore(order: OrderModel): Result<OrderModel> {
        return try {
            val orderRef = firestore.collection("Orders").document() // Tạo document mới
            val updatedOrder = order.copy(
                orderCode = orderRef.id, // Lấy Firestore document ID làm mã đơn hàng
                createdAt = Timestamp.now(),
                updatedAt = Timestamp.now()
            )

            orderRef.set(updatedOrder, SetOptions.merge()).await()
            Log.d("Firestore", "Order added successfully")
            Result.success(updatedOrder)
        } catch (e: Exception) {
            Log.e("Firestore", "Error adding order", e)
            Result.failure(e)
        }
    }


    suspend fun getAllOrders(): Result<List<OrderModel>> {
        return try {
            val querySnapshot = firestore.collection("Orders")
                .get()
                .await()
            val orders = querySnapshot.documents.mapNotNull { it.toObject(OrderModel::class.java) }

            if (orders.isNotEmpty()) {
                Log.d("FirestoreRepository", "Loaded ${orders.size}")
                Result.success(orders)
            } else {
                Log.w("FirestoreRepository", "No orders found")
                Result.success(emptyList()) // Trả về danh sách rỗng thay vì lỗi
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error loading orders", e)
            Result.failure(e)
        }
    }

    suspend fun getOrderById(orderId: String): Result<OrderModel> {
        return try {
            val documentSnapshot =
                firestore.collection("Orders").document(orderId).get().await()
            val order = documentSnapshot.toObject(OrderModel::class.java)

            if (order != null) {
                Log.d("FirestoreRepository", "Loaded Order with ID $orderId: $order")
                Result.success(order) // Thành công, trả về order
            } else {
                Log.w("FirestoreRepository", "Order with ID $orderId not found")
                Result.failure(Exception("Order not found")) // Trả về lỗi nếu không tìm thấy đơn hàng
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error loading Order with ID $orderId", e)
            Result.failure(e) // Lỗi khác
        }
    }


    suspend fun updateOrderPaymentMethod(orderId: String, paymentMethod: String): Result<Unit> {
        return try {
            val orderRef = firestore.collection("Orders").document(orderId)

            // Lấy thông tin đơn hàng hiện tại
            val snapshot = orderRef.get().await()
            if (!snapshot.exists()) {
                return Result.failure(Exception("Đơn hàng không tồn tại"))
            }

            val order = snapshot.toObject(OrderModel::class.java)
                ?: return Result.failure(Exception("Lỗi chuyển đổi dữ liệu đơn hàng"))

            // Cập nhật phương thức thanh toán và thời gian cập nhật
            val updatedOrder = order.copy(
                status = OrderStatus.PENDING,
                paymentMethod = paymentMethod,
                updatedAt = Timestamp.now()
            )
            // Dùng set với SetOptions.merge() để giữ nguyên các field khác
            orderRef.set(updatedOrder, SetOptions.merge()).await()

            Log.d(
                "FirestoreRepository",
                "Cập nhật phương thức thanh toán thành công: $paymentMethod"
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Lỗi khi cập nhật phương thức thanh toán", e)
            Result.failure(e)
        }
    }

    suspend fun getOrdersByUser(userId: String): Result<List<OrderModel>> {
        return try {
            val querySnapshot = firestore.collection("Orders")
                .whereEqualTo("userId", userId) // Lọc theo userId
                .get()
                .await()

            val orders = querySnapshot.documents.mapNotNull { it.toObject(OrderModel::class.java) }

            if (orders.isNotEmpty()) {
                Log.d("FirestoreRepository", "Loaded ${orders.size} orders for userID $userId")
                Result.success(orders)
            } else {
                Log.w("FirestoreRepository", "No orders found for userID: $userId")
                Result.success(emptyList()) // Trả về danh sách rỗng thay vì lỗi
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error loading orders for userID: $userId", e)
            Result.failure(e)
        }
    }

//    suspend fun cancelOrder(orderId: String): Result<Unit> {
//        return try {
//            val orderRef = firestore.collection("Orders").document(orderId)
//
//            // Lấy thông tin đơn hàng hiện tại
//            val snapshot = orderRef.get().await()
//            if (!snapshot.exists()) {
//                return Result.failure(Exception("Đơn hàng không tồn tại"))
//            }
//
//            val order = snapshot.toObject(OrderModel::class.java)
//                ?: return Result.failure(Exception("Lỗi chuyển đổi dữ liệu đơn hàng"))
//
//            // Cập nhật trạng thái đơn hàng thành CANCELED
//            val updatedOrder = order.copy(
//                status = OrderStatus.CANCELED,
//                updatedAt = Timestamp.now()
//            )
//
//            // Cập nhật đơn hàng trên Firestore
//            orderRef.set(updatedOrder, SetOptions.merge()).await()
//
//            Log.d("FirestoreRepository", "Đơn hàng đã được hủy thành công!")
//            Result.success(Unit)
//        } catch (e: Exception) {
//            Log.e("FirestoreRepository", "Lỗi khi hủy đơn hàng", e)
//            Result.failure(e)
//        }
//    }

    suspend fun updateOrderStatus(orderId: String, newStatus: OrderStatus): Result<Unit> {
        return try {
            val orderRef = firestore.collection("Orders").document(orderId)

            // Lấy thông tin đơn hàng hiện tại
            val snapshot = orderRef.get().await()
            if (!snapshot.exists()) {
                return Result.failure(Exception("Đơn hàng không tồn tại"))
            }

            val order = snapshot.toObject(OrderModel::class.java)
                ?: return Result.failure(Exception("Lỗi chuyển đổi dữ liệu đơn hàng"))

            // Cập nhật trạng thái đơn hàng
            val updatedOrder = order.copy(
                status = newStatus,
                updatedAt = Timestamp.now()
            )

            // Cập nhật trên Firestore
            orderRef.set(updatedOrder, SetOptions.merge()).await()

            Log.d("FirestoreRepository", "Đơn hàng đã được cập nhật trạng thái thành $newStatus!")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Lỗi khi cập nhật trạng thái đơn hàng", e)
            Result.failure(e)
        }
    }



}