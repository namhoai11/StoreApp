package com.example.storeapp.ui.screen.admin.manage.product.add_product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import com.example.storeapp.model.ColorOptions
import com.example.storeapp.model.ProductOptions
import com.example.storeapp.model.StockByVariant
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddProductViewModel(
    private val repository: FirebaseFireStoreRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddProductUiState())
    val uiState: StateFlow<AddProductUiState> = _uiState.asStateFlow()

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(productDetailsItem = it.productDetailsItem.copy(name = newName)) }
    }

    fun onPriceChange(newPrice: String) {
        _uiState.update { it.copy(priceInput = newPrice) }
    }

    fun onDescriptionChange(newDescription: String) {
        _uiState.update { it.copy(productDetailsItem = it.productDetailsItem.copy(description = newDescription)) }
    }

    fun addImage(color: String, imageUrl: String) {
        val updatedColors = _uiState.value.productDetailsItem.availableOptions?.listColorOptions?.toMutableList() ?: mutableListOf()
        updatedColors.add(ColorOptions(color, imageUrl))
        _uiState.update {
            it.copy(productDetailsItem = it.productDetailsItem.copy(
                availableOptions = it.productDetailsItem.availableOptions?.copy(listColorOptions = updatedColors)
            ))
        }
    }

    fun addOption(optionName: String, price: String) {
        val optionPrice = price.toDoubleOrNull() ?: 0.0
        val updatedOptions = _uiState.value.productDetailsItem.availableOptions?.listProductOptions?.toMutableList() ?: mutableListOf()
        updatedOptions.add(ProductOptions(optionName, optionPrice))
        _uiState.update {
            it.copy(productDetailsItem = it.productDetailsItem.copy(
                availableOptions = it.productDetailsItem.availableOptions?.copy(listProductOptions = updatedOptions)
            ))
        }
    }

    fun addStock(color: String, option: String, quantity: String) {
        val stockQuantity = quantity.toIntOrNull() ?: 0
        val updatedStock = _uiState.value.productDetailsItem.stockByVariant.toMutableList()
        updatedStock.add(StockByVariant(color, option, stockQuantity))
        _uiState.update { it.copy(productDetailsItem = it.productDetailsItem.copy(stockByVariant = updatedStock)) }
    }

    fun addOrUpdateProduct() {
        if (!validateProduct()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val price = _uiState.value.priceInput.toDoubleOrNull() ?: 0.0

            val updatedProduct = _uiState.value.productDetailsItem.copy(
                price = price,
                stockQuantity = _uiState.value.productDetailsItem.stockByVariant.sumOf { it.quantity },
                updatedAt = Timestamp.now()
            )

            val result = repository.addOrUpdateProduct(updatedProduct)
            result.onSuccess {
                _uiState.update { it.copy(successMessage = "Sản phẩm đã được thêm thành công!", isLoading = false) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
            }
        }
    }

    private fun validateProduct(): Boolean {
        val product = _uiState.value.productDetailsItem

        if (product.name.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Tên sản phẩm không được để trống") }
            return false
        }

        val price = _uiState.value.priceInput.toDoubleOrNull()
        if (price == null || price <= 0) {
            _uiState.update { it.copy(errorMessage = "Giá sản phẩm phải lớn hơn 0") }
            return false
        }

        if (product.description.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Mô tả không được để trống") }
            return false
        }

        _uiState.update { it.copy(errorMessage = null) }
        return true
    }
}
