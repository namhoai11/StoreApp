package com.example.storeapp.ui.screen.cart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import com.example.storeapp.model.ProductsOnCart
import com.example.storeapp.model.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(
    private val repository: FirebaseFireStoreRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState

    private val _user = MutableLiveData<UserModel?>()
    val user: LiveData<UserModel?> = _user

    init {
        loadUser()
        observeProductsChanges()
//         Quan sát trạng thái của uiState
        viewModelScope.launch {
            _uiState.collect { state ->
                Log.d("CartViewModel", "Current UI State: $state")
            }
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            val userData = repository.getCurrentUser()
            _user.value = userData
            Log.d("CartViewModel", "User loaded: $userData")
//            loadProduct()
            loadCart()
        }
    }

    private fun loadCart() = viewModelScope.launch {
        try {
            _uiState.update { it.copy(showCartLoading = true) }
            val cart = _user.value?.let { repository.getPCartByUser(it.id) }
            Log.d("CartViewmodel", "cart: $cart")
            if (cart != null) {
                val listProductToShow = mutableListOf<ProductsOnCartToShow>()
                cart.products.forEach { productOnCart ->
                    val product = repository.getProductById(productOnCart.productId)
                    if (product != null) {
                        val selectedOption = product.availableOptions?.listProductOptions
                            ?.find { it.optionsName == productOnCart.productOptions }
                        val price = selectedOption?.priceForOptions ?: product.price

                        Log.d("CartViewModel", "productOnCartPrice:$price")
//                        val selectedColor = product.availableOptions?.listColorOptions
//                            ?.find { it.colorName == productOnCart.colorOptions }
//                        val color = selectedColor?.colorName ?: ""
                        val remainingStock = product.stockByVariant.find {
                            it.colorName == productOnCart.colorOptions && it.optionName == productOnCart.productOptions
                        }?.quantity ?: 0
                        Log.d("CartViewModel", "remainingStock :$remainingStock")
                        val notEnough =
                            if (productOnCart.quantity > remainingStock) "sản phẩm trong kho không đủ" else ""
                        val productOnCartToShow = ProductsOnCartToShow(
                            productId = productOnCart.productId,
                            productName = productOnCart.productName,
                            productImage = productOnCart.productImage,
                            productPrice = price,
                            productOptions = productOnCart.productOptions,
                            colorOptions = productOnCart.colorOptions,
                            quantity = productOnCart.quantity,
                            notExist = "",
                            notEnough = notEnough
                        )
                        Log.d("CartViewModel", "productOnCartToShow :$productOnCartToShow")

                        listProductToShow.add(productOnCartToShow)
                    } else {
                        val productOnCartToShow = ProductsOnCartToShow(
                            productId = productOnCart.productId,
                            productName = productOnCart.productName,
                            productImage = productOnCart.productImage,
                            productPrice = 0.0,
                            productOptions = productOnCart.productOptions,
                            colorOptions = productOnCart.colorOptions,
                            quantity = productOnCart.quantity,
                            notExist = "Sản phẩm ngừng kinh doanh",
                            notEnough = "",
                        )
                        listProductToShow.add(productOnCartToShow)
                    }

                }
                Log.d("CartViewModel", "listProductToShow :$listProductToShow")
                val totalPrice = listProductToShow.sumOf { it.productTotalPrice }
                _uiState.update {
                    it.copy(
                        listProductOnCart = listProductToShow,
                        totalPrice = totalPrice,
                        showCartLoading = false,
                    )
                }
            } else {
                Log.e("CartViewmodel", "Cart not found for UserId: ${_user.value?.id}")
                // Cập nhật trạng thái lỗi vào UI State
                _uiState.update {
                    it.copy(
                        showCartLoading = false,
                        errorMessage = "Cart not found for UserId: ${_user.value?.id}"
                    )
                }
            }
        } catch (e: Exception) {
            // Xử lý lỗi nếu cần (thêm trạng thái lỗi vào UiState hoặc log)
            Log.e("CartViewmodel", "Error Cart: ${e.message}")
            // Xử lý lỗi chung, cập nhật trạng thái lỗi
            _uiState.update {
                it.copy(
                    showCartLoading = false,
                    errorMessage = "Error loading Cart: ${e.message}"
                )
            }
        }
    }

    fun increaseClicked(productsOnCartToShow: ProductsOnCartToShow) {
        viewModelScope.launch {
            val currentUserId = _user.value?.id
            if (currentUserId == null) {
                _uiState.update { it.copy(errorMessage = "Không thể xác định người dùng") }
                return@launch
            }
//            _uiState.update { currentState ->
//                val updatedList = currentState.listProductOnCart.map {
//                    if (it.productId == productsOnCartToShow.productId &&
//                        it.productOptions == productsOnCartToShow.productOptions &&
//                        it.colorOptions == productsOnCartToShow.colorOptions
//                    ) {
//                        it.copy(quantity = it.quantity + 1)
//                    } else it
//                }
//                currentState.copy(listProductOnCart = updatedList)
//            }
//            val productOnCart = ProductsOnCart(
//                productId = productsOnCartToShow.productId,
//                productName = productsOnCartToShow.productName,
//                productImage = productsOnCartToShow.productImage,
//                productOptions = productsOnCartToShow.productOptions,
//                colorOptions = productsOnCartToShow.colorOptions,
//                quantity = productsOnCartToShow.quantity
//            )
//            val result = repository.updateProductQuantityInCart(
//                currentUserId,
//                productOnCart,
//                CartAction.Increase
//            )

            // Lưu trạng thái cũ để rollback nếu lỗi
            var oldList = emptyList<ProductsOnCartToShow>()
            var oldTotalPrice = 0.0

            _uiState.update { currentState ->
                oldList = currentState.listProductOnCart // Lưu danh sách cũ
                oldTotalPrice = currentState.totalPrice  // Lưu giá cũ

                val updatedList = currentState.listProductOnCart.map {
                    if (it.matches(productsOnCartToShow)) {
                        val updatedProduct = it.copy(quantity = it.quantity + 1)
                        updatedProduct.copy(productTotalPrice = updatedProduct.productPrice * updatedProduct.quantity)
                    } else it
                }

                // Cập nhật tổng giá mới
                val newTotalPrice = updatedList.sumOf { it.productTotalPrice }

                currentState.copy(
                    listProductOnCart = updatedList,
                    totalPrice = newTotalPrice
                )
            }

            // Gọi API tăng số lượng
            val result = repository.updateProductQuantityInCart(
                currentUserId, productsOnCartToShow.toProductsOnCart(), CartAction.Increase
            )

//            if (result.isSuccess) {
//                _uiState.update {
//                    it.copy(
//                        successMessage = "Đã tăng số lượng sản phẩm",
//                        showCartLoading = false
//                    )
//                }
//                loadCart() // Cập nhật giỏ hàng sau khi thay đổi
//            } else {
//                _uiState.update {
//                    it.copy(
//                        errorMessage = "Lỗi khi cập nhật giỏ hàng: ${result.exceptionOrNull()?.message}",
//                        showCartLoading = false
//                    )
//                }
//
//            }

            if (result.isFailure) {
                // Rollback nếu lỗi
                _uiState.update {
                    it.copy(
                        listProductOnCart = oldList,
                        totalPrice = oldTotalPrice,
                        errorMessage = "Lỗi khi giảm số lượng sản phẩm: ${result.exceptionOrNull()?.message}"
                    )
                }
                Log.e(
                    "CartViewModel",
                    "Lỗi khi giảm số lượng sản phẩm: ${result.exceptionOrNull()?.message}"
                )
            }
        }
    }

    fun decreaseClicked(productsOnCartToShow: ProductsOnCartToShow) {
        viewModelScope.launch {
            val currentUserId = _user.value?.id
            if (currentUserId == null) {
                _uiState.update { it.copy(errorMessage = "Không thể xác định người dùng") }
                return@launch
            }

            if (productsOnCartToShow.quantity == 1) {
                // Nếu số lượng = 1, gọi hàm xoá thay vì giảm
//                confirmRemoveClicked(productsOnCartToShow)
                onRemoveClick(productsOnCartToShow)
                return@launch
            }

            // Lưu trạng thái cũ để rollback nếu lỗi
            var oldList = emptyList<ProductsOnCartToShow>()
            var oldTotalPrice = 0.0

            _uiState.update { currentState ->
                oldList = currentState.listProductOnCart // Lưu danh sách cũ
                oldTotalPrice = currentState.totalPrice  // Lưu giá cũ

                val updatedList = currentState.listProductOnCart.map {
                    if (it.matches(productsOnCartToShow)) {
                        val updatedProduct = it.copy(quantity = it.quantity - 1)
                        updatedProduct.copy(productTotalPrice = updatedProduct.productPrice * updatedProduct.quantity)
                    } else it
                }

                // Cập nhật tổng giá mới
                val newTotalPrice = updatedList.sumOf { it.productTotalPrice }

                currentState.copy(
                    listProductOnCart = updatedList,
                    totalPrice = newTotalPrice
                )
            }

            // Gọi API giảm số lượng
            val result = repository.updateProductQuantityInCart(
                currentUserId, productsOnCartToShow.toProductsOnCart(), CartAction.Decrease
            )

            if (result.isFailure) {
                // Rollback nếu lỗi
                _uiState.update {
                    it.copy(
                        listProductOnCart = oldList,
                        totalPrice = oldTotalPrice,
                        errorMessage = "Lỗi khi giảm số lượng sản phẩm: ${result.exceptionOrNull()?.message}"
                    )
                }
                Log.e(
                    "CartViewModel",
                    "Lỗi khi giảm số lượng sản phẩm: ${result.exceptionOrNull()?.message}"
                )
            }
        }
    }


    fun onRemoveClick(product: ProductsOnCartToShow) {
        _uiState.update { it.copy(isShowConfirmRemovedDialog = true, productSelected = product) }
    }

    fun confirmRemoveClicked(product: ProductsOnCartToShow? = _uiState.value.productSelected) {
        viewModelScope.launch {
            val currentUserId = _user.value?.id
            if (product == null || currentUserId == null) {
                _uiState.update { it.copy(errorMessage = "Không thể xác định sản phẩm hoặc người dùng") }
                return@launch
            }

            // Lưu trạng thái cũ để rollback nếu có lỗi
            var oldList = emptyList<ProductsOnCartToShow>()
            var oldTotalPrice = 0.0

            _uiState.update { currentState ->
                oldList = currentState.listProductOnCart // Lưu danh sách cũ
                oldTotalPrice = currentState.totalPrice  // Lưu giá cũ

                // Cập nhật danh sách sản phẩm sau khi xóa
                val updatedList = currentState.listProductOnCart.filterNot {
                    it.matches(product) // Xóa sản phẩm khỏi danh sách
                }

                // Tính toán lại tổng giá
                val newTotalPrice = updatedList.sumOf { it.productTotalPrice }

                currentState.copy(
                    listProductOnCart = updatedList,
                    totalPrice = newTotalPrice, // Cập nhật tổng giá
                    isShowConfirmRemovedDialog = false,
                    productSelected = null
                )
            }

            // Gọi API để xóa sản phẩm khỏi giỏ hàng
            val result = repository.updateProductQuantityInCart(
                currentUserId,
                product.toProductsOnCart(),
                CartAction.Remove
            )

            // Kiểm tra kết quả trả về từ API
            if (result.isFailure) {
                // Nếu có lỗi, thực hiện rollback và hiển thị thông báo lỗi
                _uiState.update {
                    it.copy(
                        listProductOnCart = oldList,
                        totalPrice = oldTotalPrice,
                        errorMessage = "Lỗi khi xoá sản phẩm: ${result.exceptionOrNull()?.message}"
                    )
                }
                Log.e("CartViewModel", "Lỗi khi xoá sản phẩm: ${result.exceptionOrNull()?.message}")
            } else {
                // Nếu thành công, cập nhật trạng thái UI
                _uiState.update { it.copy(successMessage = "Đã xoá sản phẩm") }
            }
        }
    }


    fun dismissRemoveDialog() {
        _uiState.update { it.copy(isShowConfirmRemovedDialog = false, productSelected = null) }
    }

    /**
     * Rollback nếu có lỗi từ server
     */
    private fun rollback(product: ProductsOnCartToShow, rollbackAmount: Int, errorMessage: String) {
        _uiState.update { currentState ->
            val rollbackList = currentState.listProductOnCart.map {
                if (it.matches(product)) it.copy(quantity = it.quantity + rollbackAmount) else it
            }
            currentState.copy(listProductOnCart = rollbackList, errorMessage = errorMessage)
        }
    }

    /**
     * Extension function để kiểm tra 2 sản phẩm có giống nhau không
     */
    private fun ProductsOnCartToShow.matches(other: ProductsOnCartToShow): Boolean {
        return this.productId == other.productId &&
                this.productOptions == other.productOptions &&
                this.colorOptions == other.colorOptions
    }

    /**
     * Chuyển đổi từ ProductsOnCartToShow sang ProductsOnCart
     */
    private fun ProductsOnCartToShow.toProductsOnCart(): ProductsOnCart {
        return ProductsOnCart(
            productId = this.productId,
            productName = this.productName,
            productImage = this.productImage,
            productOptions = this.productOptions,
            colorOptions = this.colorOptions,
            quantity = this.quantity
        )
    }

    private fun observeProductsChanges() {
        viewModelScope.launch {
            _uiState.collectLatest { uiState ->
                val productIds = uiState.listProductOnCart.map { it.productId }.distinct()

                repository.observeProductsByListId(productIds).collectLatest { updatedProducts ->
                    val updatedCart = uiState.listProductOnCart.map { cartItem ->
                        val updatedProduct = updatedProducts.find { it.id == cartItem.productId }

                        if (updatedProduct != null) {
                            val selectedOption = updatedProduct.availableOptions?.listProductOptions
                                ?.find { it.optionsName == cartItem.productOptions }

                            val newPrice = selectedOption?.priceForOptions ?: updatedProduct.price
                            val remainingStock = updatedProduct.stockByVariant.find {
                                it.colorName == cartItem.colorOptions && it.optionName == cartItem.productOptions
                            }?.quantity ?: 0

                            val notEnough =
                                if (cartItem.quantity > remainingStock) "Sản phẩm trong kho không đủ" else ""
                            Log.d("CartViewModel", "newPriceOnObserveProduct: $newPrice")
                            Log.d(
                                "CartViewModel",
                                "remainingStockOnObserveProduct: $remainingStock"
                            )

                            cartItem.copy(
                                productPrice = newPrice,
                                productTotalPrice = newPrice * cartItem.quantity,
                                notEnough = notEnough
                            )
                        } else {
                            cartItem.copy(
                                productPrice = 0.0,
                                notExist = "Sản phẩm ngừng kinh doanh"
                            )
                        }
                    }

                    _uiState.update { currentUiState ->
                        currentUiState.copy(
                            listProductOnCart = updatedCart,
                            totalPrice = updatedCart.sumOf { it.productTotalPrice }
                        )
                    }
                    Log.d("CartViewModel", "updatedCart: $updatedCart")

                }
            }
        }
    }

}