package com.example.storeapp.ui.screen.productdetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import com.example.storeapp.model.ProductsOnCart
import com.example.storeapp.model.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: FirebaseFireStoreRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState

    private val _user = MutableLiveData<UserModel?>()
    val user: LiveData<UserModel?> = _user


    private val productDetailsId: Int =
        checkNotNull(savedStateHandle[ProductDetailsDestination.productDetailsIdArg])


    init {
        loadUser()
//         Quan sát trạng thái của uiState
        viewModelScope.launch {
            _uiState.collect { state ->
                Log.d("ProductDetailsViewmodel", "Current UI State: $state")
            }
        }
//        observeStockChanges() // Lắng nghe thay đổi số lượng trong Realtime Database
        observeProductChanges() // Lắng nghe thay đổi số lượng trong dataStore
    }

    private fun loadUser() {
        viewModelScope.launch {
            val userData = repository.getCurrentUser()
            _user.value = userData
            Log.d("ProductDetailsViewmodel", "User loaded: $userData")
            loadProduct()

        }
    }

    private fun loadProduct() = viewModelScope.launch {
        try {
            _uiState.update { it.copy(showProductDetailsLoading = true) }

            val productDetails = repository.getProductById(productDetailsId.toString())

            Log.d("ProductDetailsViewmodel", "productDetails: $productDetails")


            if (productDetails != null) {

                // Lấy Stock từ Realtime Database
//                val stock = realtimeDatabase.loadStockByProductId(productDetailsId.toString())

                val isWishlistItem =
                    _user.value?.wishList?.contains(productDetailsId.toString()) ?: false

                Log.d("ProductDetailsViewmodel", "isWishlistItem: $isWishlistItem")

                //Load ListColorOption
                val listColorOption =
                    productDetails.availableOptions?.listColorOptions ?: emptyList()
                Log.d("ProductDetailsViewmodel", "listColorOptions: $listColorOption")

                //Load ListProductOption
                val listProductOption =
                    productDetails.availableOptions?.listProductOptions ?: emptyList()
                Log.d("ProductDetailsViewmodel", "listProductOptions: $listProductOption")

                //Load ListStandardImage
                val listStandardImage = productDetails.images.toMutableList()
                productDetails.availableOptions?.listColorOptions?.forEachIndexed { _, value ->
                    listStandardImage += value.imagesColor
                }
                Log.d("ProductDetailsViewmodel", "lisStandardImage: $listStandardImage")

                val currentImage = listStandardImage.firstOrNull() ?: ""

                Log.d("ProductDetailsViewmodel", "currentImage: $currentImage")


                val currentPrice = productDetails.price

                Log.d("ProductDetailsViewmodel", "currentPrice: $currentPrice")

                val stockByVariant = productDetails.stockByVariant.sumOf { it.quantity }
                Log.d("ProductDetailsViewmodel", "stockByVariant: $stockByVariant")

                _uiState.update { it ->
                    it.copy(
                        productDetailsItem = productDetails,
                        isWishListItem = isWishlistItem,
                        listColorOptions = listColorOption,
                        listProductOptions = listProductOption,
                        listStandardImages = listStandardImage,
                        currentPrice = currentPrice,
//                        stock = stock,
                        currentQuantity = stockByVariant,
                        showProductDetailsLoading = false
                    )
                }
                selectStandardImages(
                    selectedIndex = 0,
                )
                Log.d(
                    "ProductDetailsViewmodel",
                    "currentImageUiState: ${_uiState.value.currentImage}"
                )

            } else {
                Log.e("ProductDetailsViewmodel", "Product not found for id: $productDetailsId")
                // Cập nhật trạng thái lỗi vào UI State
                _uiState.update {
                    it.copy(
                        showProductDetailsLoading = false,
                        errorMessage = "Product not found for id: $productDetailsId"
                    )
                }
            }
        } catch (e: Exception) {
            // Xử lý lỗi nếu cần (thêm trạng thái lỗi vào UiState hoặc log)
            Log.e("ProductDetailsViewmodel", "Error loading product details: ${e.message}")
            // Xử lý lỗi chung, cập nhật trạng thái lỗi
            _uiState.update {
                it.copy(
                    showProductDetailsLoading = false,
                    errorMessage = "Error loading product details: ${e.message}"
                )
            }
        }
    }

//    private fun observeStockChanges() {
//        viewModelScope.launch {
//            realtimeDatabase.observeStockByProductId(productDetailsId.toString()).collect { stock ->
//                if (stock != null) {
//                    _uiState.update {
//                        it.copy(
//                            currentQuantity = stock.stockByVariant.sumOf { stock -> stock.quantity },
////                            stock = stock
//                        )
//                    }
//                    Log.d("ProductDetailsViewmodel", "Updated stock: ${stock.stockQuantity}")
//                }
//            }
//        }
//    }

    private fun observeProductChanges() {
        viewModelScope.launch {
            repository.observeProductById(productDetailsId.toString()).collect { product ->
                if (product != null) {
                    _uiState.update {
                        it.copy(
                            currentQuantity = product.stockByVariant.sumOf { stock -> stock.quantity },
                            listProductOptions = product.availableOptions!!.listProductOptions
//                            stock = stock
                        )
                    }
                    if (_uiState.value.selectedOptionIndex == -1) {
                        _uiState.update {
                            it.copy(
                                currentPrice = product.price
                            )
                        }
                    } else if (product.availableOptions!!.listProductOptions.isNotEmpty()) {
                        _uiState.update {
                            it.copy(
                                currentPrice = product.availableOptions.listProductOptions[_uiState.value.selectedOptionIndex].priceForOptions
                            )
                        }
                    }
                    Log.d(
                        "ProductDetailsViewmodel",
                        "Updated stock: ${_uiState.value.currentQuantity}"
                    )
                }
            }
        }
    }

    fun selectStandardImages(
        selectedIndex: Int,
    ) {
        val currentImage = _uiState.value.listStandardImages[selectedIndex]
        _uiState.update {
            it.copy(
                selectedStandardImageUrl = selectedIndex,
                selectedColorUrl = -1,
                currentImage = currentImage
            )
        }
    }

    fun selectColorImages(
        selectedIndex: Int,
    ) {
        if (selectedIndex == _uiState.value.selectedColorUrl) {
            _uiState.update {
                it.copy(
                    selectedColorUrl = -1,
                )
            }
        } else {
            val stockByVariant =
                _uiState.value.productDetailsItem.stockByVariant.filter { it.colorName == _uiState.value.listColorOptions[selectedIndex].colorName }
            val currentImage = _uiState.value.listColorOptions[selectedIndex].imagesColor
            _uiState.update { it ->
                it.copy(
                    selectedColorUrl = selectedIndex,
                    selectedStandardImageUrl = -1,
                    currentImage = currentImage,
                    currentQuantity = stockByVariant.sumOf { stock -> stock.quantity }
                )
            }
        }

    }

    fun selectedOptions(
        selectedIndex: Int
    ) {
        if (selectedIndex == _uiState.value.selectedOptionIndex) {
            _uiState.update {
                it.copy(
                    selectedOptionIndex = -1
                )
            }
        } else {
            val stockByVariant =
                _uiState.value.productDetailsItem.stockByVariant.filter { it.optionName == _uiState.value.listProductOptions[selectedIndex].optionsName }

            val price = _uiState.value.listProductOptions[selectedIndex].priceForOptions
            _uiState.update {
                it.copy(
                    selectedOptionIndex = selectedIndex,
                    currentPrice = price,
                    currentQuantity = stockByVariant.sumOf { stock -> stock.quantity }
                )
            }
        }

    }

    fun favoriteClick() {
        viewModelScope.launch {
            val isWishListItem = _uiState.value.isWishListItem
            _uiState.update { it.copy(isWishListItem = !isWishListItem) }
            if (isWishListItem) {
                _user.value?.let {
                    repository.removeWishListItem(
                        it.id,
                        productDetailsId.toString()
                    )
                }
            } else {
                _user.value?.let { repository.addWishListItem(it.id, productDetailsId.toString()) }
            }
        }
    }

    fun buyClick() {
        Log.d("ProductDetailsViewModel", "buyClick")

        if (!validateCartInputs()) return
        _uiState.update { it.copy(isAddCartLoading = true, errorMessage = "") }

        viewModelScope.launch {
            val currentUserId = _user.value?.id
            if (currentUserId == null) {
                _uiState.update { it.copy(errorMessage = "Không thể xác định sản phẩm hoặc người dùng") }
                return@launch
            }

            val selectedProductOption = if (_uiState.value.listProductOptions.isNotEmpty() &&
                _uiState.value.selectedOptionIndex in _uiState.value.listProductOptions.indices
            ) {
                _uiState.value.listProductOptions[_uiState.value.selectedOptionIndex].optionsName
            } else {
                ""
            }


            val selectedColorOption = if (_uiState.value.listColorOptions.isNotEmpty() &&
                _uiState.value.selectedColorUrl in _uiState.value.listColorOptions.indices
            ) {
                _uiState.value.listColorOptions[_uiState.value.selectedColorUrl].colorName
            } else {
                ""
            }

            val productsOnCart = ProductsOnCart(
                productId = _uiState.value.productDetailsItem.id,
                productName = _uiState.value.productDetailsItem.name,
                productImage = _uiState.value.currentImage,
//                productPrice = _uiState.value.currentPrice,
                productOptions = selectedProductOption ?: "",
                colorOptions = selectedColorOption,
                quantity = 1
            )
            val result =repository.addProductToCartUseSet(currentUserId, productsOnCart)

            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        isAddCartLoading = false,
                        successMessage = "Thêm vào giỏ hàng thành công!",
                        isShowConfirmDialog = true
                    )
                }

            } else {
                _uiState.update {
                    it.copy(
                        isAddCartLoading = false,
                        errorMessage = result.exceptionOrNull()?.message ?: "Có lỗi xảy ra"
                    )
                }
            }

        }
    }

    private fun validateCartInputs(): Boolean {
        return when {
            _uiState.value.productDetailsItem.id == "-1" -> {
                _uiState.update { it.copy(errorMessage = "Sản phẩm không hợp lệ") }
                Log.e("ProductDetailsViewModel", _uiState.value.errorMessage)

                false
            }

//            _uiState.value.currentQuantity <= 0 -> {
//                _uiState.update { it.copy(errorMessage = "Vui lòng chọn số lượng hợp lệ") }
//                false
//            }

            // Chỉ kiểm tra selectedOptionIndex nếu danh sách có phần tử
            _uiState.value.listProductOptions.isNotEmpty() &&
                    (_uiState.value.selectedOptionIndex !in _uiState.value.listProductOptions.indices) -> {
                _uiState.update {
                    it.copy(
                        errorMessage = "Vui lòng chọn một tùy chọn sản phẩm",
                        isShowAlertDialog = true
                    )
                }
                Log.e("ProductDetailsViewModel", _uiState.value.errorMessage)
                false
            }

            // Chỉ kiểm tra selectedColorUrl nếu danh sách có phần tử
            _uiState.value.listColorOptions.isNotEmpty() &&
                    (_uiState.value.selectedColorUrl !in _uiState.value.listColorOptions.indices) -> {
                _uiState.update {
                    it.copy(
                        errorMessage = "Vui lòng chọn một màu sắc",
                        isShowAlertDialog = true
                    )
                }
                Log.e("ProductDetailsViewModel", _uiState.value.errorMessage)

                false
            }


            else -> true
        }
    }

    fun exitDialog() {
        _uiState.update {
            it.copy(
                isShowConfirmDialog = false
            )
        }
    }

    fun conFirmAlertDialog() {
        _uiState.update {
            it.copy(
                isShowAlertDialog = false
            )
        }
    }

}