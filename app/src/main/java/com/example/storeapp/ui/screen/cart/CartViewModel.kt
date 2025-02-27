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

    //Dùng cho HomeScreen
    fun resetCartUiState() {
        _uiState.value = CartUiState() // Reset về trạng thái mặc định
        loadCart() // Gọi lại hàm load giỏ hàng
    }


    private fun loadCart() = viewModelScope.launch {
        try {
            _uiState.update { it.copy(showCartLoading = true) }

            val currentUser = _user.value
            if (currentUser == null) {
                _uiState.update { it.copy(showCartLoading = false, errorMessage = "Không thể xác định người dùng") }
                return@launch
            }

            val cart = repository.getCartByUser(currentUser.id)
            Log.d("CartViewModel", "cart: $cart")

            if (cart == null) {
                Log.e("CartViewModel", "Cart not found for UserId: ${currentUser.id}")
                _uiState.update {
                    it.copy(
                        showCartLoading = false,
                        errorMessage = "Cart not found for UserId: ${currentUser.id}"
                    )
                }
                return@launch
            }

            val productIds = cart.products.map { it.productId }
            val products = repository.getProductByListId(productIds).associateBy { it.id }

            val listProductToShow = cart.products.map { productOnCart ->
                val product = products[productOnCart.productId]

                if (product != null) {
                    val selectedOption = product.availableOptions?.listProductOptions
                        ?.find { it.optionsName == productOnCart.productOptions }
                    val price = selectedOption?.priceForOptions ?: product.price

                    val remainingStock = product.stockByVariant.find {
                        it.colorName == productOnCart.colorOptions && it.optionName == productOnCart.productOptions
                    }?.quantity ?: 0

                    val notEnough =
                        if (productOnCart.quantity > remainingStock) "sản phẩm trong kho không đủ" else ""

                    ProductsOnCartToShow(
                        productId = productOnCart.productId,
                        productName = productOnCart.productName,
                        productImage = productOnCart.productImage,
                        productPrice = price,
                        productOptions = productOnCart.productOptions,
                        colorOptions = productOnCart.colorOptions,
                        quantity = productOnCart.quantity,
                        remainingStock=remainingStock,
                        notExist = "",
                        notEnough = notEnough
                    )
                } else {
                    ProductsOnCartToShow(
                        productId = productOnCart.productId,
                        productName = productOnCart.productName,
                        productImage = productOnCart.productImage,
                        productPrice = 0.0,
                        productOptions = productOnCart.productOptions,
                        colorOptions = productOnCart.colorOptions,
                        quantity = productOnCart.quantity,
                        remainingStock = 0,
                        notExist = "Sản phẩm ngừng kinh doanh",
                        notEnough = "",
                    )
                }
            }

            val totalPrice = listProductToShow.sumOf { it.productTotalPrice }

            _uiState.update {
                it.copy(
                    cartId = cart.id,
                    listProductOnCart = listProductToShow,
                    totalPrice = totalPrice,
                    showCartLoading = false,
                )
            }
        } catch (e: Exception) {
            Log.e("CartViewModel", "Error loading Cart: ${e.message}")
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

            // Lưu trạng thái cũ để rollback nếu lỗi
            var oldList = emptyList<ProductsOnCartToShow>()
            var oldTotalPrice = 0.0

            _uiState.update { currentState ->
                oldList = currentState.listProductOnCart // Lưu danh sách cũ
                oldTotalPrice = currentState.totalPrice  // Lưu giá cũ

                val updatedList = currentState.listProductOnCart.map {
                    if (it.matches(productsOnCartToShow)) {
                        val newQuantity = it.quantity + 1
                        val isOverStock = newQuantity > it.remainingStock
                        val updatedProduct = it.copy(
                            quantity = newQuantity,
                            productTotalPrice = it.productPrice * newQuantity,
                            notEnough = if (isOverStock) "Sản phẩm không đủ" else ""
                        )
                        updatedProduct
                    } else it
                }

                // Cập nhật tổng giá mới
                val newTotalPrice = updatedList.sumOf { it.productTotalPrice }

                currentState.copy(
                    listProductOnCart = updatedList,
                    totalPrice = newTotalPrice
                )
            }

            // Nếu số lượng vượt quá tồn kho, không gọi API
            if (productsOnCartToShow.quantity + 1 > productsOnCartToShow.remainingStock) {
                return@launch
            }

            // Gọi API tăng số lượng
            val result = repository.updateProductQuantityInCart(
                currentUserId, productsOnCartToShow.toProductsOnCart(), CartAction.Increase
            )

            if (result.isFailure) {
                // Rollback nếu lỗi
                _uiState.update {
                    it.copy(
                        listProductOnCart = oldList,
                        totalPrice = oldTotalPrice,
                        errorMessage = "Lỗi khi tăng số lượng sản phẩm: ${result.exceptionOrNull()?.message}"
                    )
                }
                Log.e(
                    "CartViewModel",
                    "Lỗi khi tăng số lượng sản phẩm: ${result.exceptionOrNull()?.message}"
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
                onRemoveClick(productsOnCartToShow)
                return@launch
            }

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

                val newTotalPrice = updatedList.sumOf { it.productTotalPrice }

                currentState.copy(
                    listProductOnCart = updatedList,
                    totalPrice = newTotalPrice
                )
            }

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
            _uiState.collectLatest { currentUiState ->
                val productIds = currentUiState.listProductOnCart.map { it.productId }.distinct()

                repository.observeProductsByListId(productIds).collectLatest { updatedProducts ->
                    val updatedCart = currentUiState.listProductOnCart.map { cartItem ->
                        val updatedProduct = updatedProducts.find { it.id == cartItem.productId }

                        if (updatedProduct != null) {
                            val selectedOption = updatedProduct.availableOptions?.listProductOptions
                                ?.find { it.optionsName == cartItem.productOptions }

                            val newPrice = selectedOption?.priceForOptions ?: updatedProduct.price
                            val remainingStock = updatedProduct.stockByVariant.find {
                                it.colorName == cartItem.colorOptions && it.optionName == cartItem.productOptions
                            }?.quantity ?: 0

                            val notEnough =
                                if (cartItem.quantity > remainingStock) "Sản phẩm không đủ" else ""
                            Log.d("CartViewModel", "newPriceOnObserveProduct: $newPrice")
                            Log.d(
                                "CartViewModel",
                                "remainingStockOnObserveProduct: $remainingStock"
                            )

                            cartItem.copy(
                                productPrice = newPrice,
                                productTotalPrice = newPrice * cartItem.quantity,
                                remainingStock = remainingStock,
                                notEnough = notEnough
                            )
                        } else {
                            cartItem.copy(
                                productPrice = 0.0,
                                remainingStock = 0,
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