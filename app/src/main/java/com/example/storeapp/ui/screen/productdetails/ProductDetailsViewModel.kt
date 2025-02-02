package com.example.storeapp.ui.screen.productdetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.StoreAppRepository
import com.example.storeapp.ui.uistate.ProductDetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: StoreAppRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState

    private val productDetailsId: String =
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
            val allItems = repository.loadAllItems()
            Log.d("ProductDetailsViewmodel", "All Item:$allItems")

            _uiState.update { it.copy(showProductDetailsLoading = true) }

            val productDetails = allItems.find { it.id == productDetailsId }
            if (productDetails != null) {
                Log.d("ProductDetailsViewmodel", "Product Item: $productDetails")
                Log.d("ProductDetailsViewmodel", "PicUrls: ${productDetails.images}")
                _uiState.update { it.copy(productDetailsItem = productDetails, showProductDetailsLoading = false) }
                Log.d("ProductDetailsViewmodel", "ProductItem_uiState_picUrl: ${_uiState.value.productDetailsItem.images.firstOrNull()}")

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

}