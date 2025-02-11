package com.example.storeapp.ui.screen.productdetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: FirebaseFireStoreRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState

    private val productDetailsId: Int =
        checkNotNull(savedStateHandle[ProductDetailsDestination.productDetailsIdArg])


    init {
        loadProduct()
//         Quan sát trạng thái của uiState
        viewModelScope.launch {
            _uiState.collect { state ->
                Log.d("ProductDetailsViewmodel", "Current UI State: $state")
            }
        }
    }


    private fun loadProduct() = viewModelScope.launch {
        try {
            val allProducts = repository.loadAllProducts()
            Log.d("ProductDetailsViewmodel", "All Product:$allProducts")

            _uiState.update { it.copy(showProductDetailsLoading = true) }

            val productDetails = allProducts.find { it.id == productDetailsId.toString() }
            if (productDetails != null) {
                Log.d("ProductDetailsViewmodel", "Product Item: $productDetails")
                Log.d("ProductDetailsViewmodel", "PicUrls: ${productDetails.images}")
                _uiState.update {
                    it.copy(
                        productDetailsItem = productDetails,
                        showProductDetailsLoading = false
                    )
                }
                Log.d(
                    "ProductDetailsViewmodel",
                    "ProductItem_uiState_picUrl: ${_uiState.value.productDetailsItem.images.firstOrNull()}"
                )
                val listColorImages = emptyList<String>().toMutableList()
                productDetails.availableOptions?.listColorOptions?.forEachIndexed { _, value ->
                    listColorImages += value.imagesColor
                }

                //Load ListColorOption
                val listColorOption =
                    productDetails.availableOptions?.listColorOptions ?: emptyList()
                Log.d("ProductDetailsViewmodel", "listColorOptions: $listColorOption")
                _uiState.update {
                    it.copy(
                        listColorOptions = listColorOption
                    )
                }

                //Load ListProductOption
                val listProductOption =
                    productDetails.availableOptions?.listProductOptions ?: emptyList()
                Log.d("ProductDetailsViewmodel", "listProductOptions: $listProductOption")
                _uiState.update {
                    it.copy(
                        listProductOptions = listProductOption
                    )
                }

                //Load ListStandardImage
                val listStandardImage = productDetails.images.toMutableList()
                productDetails.availableOptions?.listColorOptions?.forEachIndexed { _, value ->
                    listStandardImage += value.imagesColor
                }
                Log.d("ProductDetailsViewmodel", "lisStandardImage: $listStandardImage")
                _uiState.update {
                    it.copy(
                        listStandardImages = listStandardImage
                    )
                }

                val currentImage = listStandardImage.firstOrNull() ?: ""
                Log.d("ProductDetailsViewmodel", "currentImage: $currentImage")
                selectStandardImages(
                    selectedIndex = 0,
                )
                Log.d(
                    "ProductDetailsViewmodel",
                    "currentImageUiState: ${_uiState.value.currentImage}"
                )

                val currentPrice = productDetails.price
                Log.d("ProductDetailsViewmodel", "currentPrice: $currentPrice")
                _uiState.update {
                    it.copy(
                        currentPrice = currentPrice
                    )
                }

                val stockByVariant = productDetails.stockQuantity
                Log.d("ProductDetailsViewmodel", "stockByVariant: $stockByVariant")
                _uiState.update {
                    it.copy(
                        currentQuantity = stockByVariant
                    )
                }

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
        val stockByVariant =
            _uiState.value.productDetailsItem.stockByVariant
                .filter { it.colorName == _uiState.value.listColorOptions[selectedIndex].colorName }
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

    fun selectedOptions(
        selectedIndex: Int
    ) {
        val stockByVariant =
            _uiState.value.productDetailsItem.stockByVariant
                .filter { it.optionName == _uiState.value.listProductOptions[selectedIndex].optionsName }
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