package com.example.storeapp.ui.screen.admin.manage.product.add_product

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
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
    savedStateHandle: SavedStateHandle,
    private val repository: FirebaseFireStoreRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddProductUiState())
    val uiState: StateFlow<AddProductUiState> = _uiState.asStateFlow()

    private val couponId: String? = savedStateHandle["productId"]
    private val isEditing: Boolean = savedStateHandle["isEditing"] ?: false

    init {
        loadCategory()
//         Quan sát trạng thái của uiState
        viewModelScope.launch {
            _uiState.collect { state ->
                Log.d("AddProductViewModel", "Current UI State: $state")
            }
        }

    }

    private fun loadCategory() = viewModelScope.launch {
        try {
            val categories = repository.loadCategory()
            Log.d("AddProductViewModel", "list categories:$categories")
            _uiState.update { it.copy(listCategory = categories, isEditing = isEditing) }

        } catch (e: Exception) {
            // Xử lý lỗi nếu cần (thêm trạng thái lỗi vào UiState hoặc log)
        }
    }

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(productDetailsItem = it.productDetailsItem.copy(name = newName)) }
    }

    fun onCategoryNameSelected(categoryName: String) {
        val categoryIdSelected =
            _uiState.value.listCategory.find { it.name == categoryName }?.id ?: return
        _uiState.update { currentState ->
            currentState.copy(
                productDetailsItem = currentState.productDetailsItem.copy(
                    categoryId = categoryIdSelected.toString()
                ),
                categoryNameSelected = categoryName
            )
        }
    }

    fun onPriceChange(newPrice: String) {
        _uiState.update {
            it.copy(
                productDetailsItem = it.productDetailsItem.copy(
                    price = newPrice.toDoubleOrNull() ?: 0.0
                ),
                priceInput = newPrice
            )
        }
    }

    fun onImageSelected(imageUri: Uri) {
        _uiState.update {
            it.copy(
                currentImageSelected = imageUri
            )
        }
    }

    fun onDoneImageClick() {
        val currentUri = _uiState.value.currentImageSelected
        if (currentUri != null) { // Chỉ thêm nếu currentUri khác null
            _uiState.update {
                it.copy(
                    listImageUriSelected = it.listImageUriSelected + currentUri,
                    currentImageSelected = null

                )
            }
            Log.d("AddProductViewModel", "listImage: ${_uiState.value.listImageUriSelected}")
        }
    }


    fun onDeleteImageClick(index: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                listImageUriSelected = currentState.listImageUriSelected.toMutableList().apply {
                    if (index in indices) removeAt(index)
                }
            )
        }
    }

    fun onEditImageChange(index: Int, newUri: Uri) {
        _uiState.update { currentState ->
            currentState.copy(
                listImageUriSelected = currentState.listImageUriSelected.toMutableList().apply {
                    if (index in indices) this[index] = newUri
                }
            )
        }
    }

    fun onOptionNameChange(optionName: String) {
        _uiState.update {
            it.copy(
                optionName = optionName
            )
        }
    }

    fun onOptionPriceChange(optionPrice: String) {
        _uiState.update {
            it.copy(
                priceForOption = optionPrice.toDoubleOrNull() ?: 0.0
            )
        }
    }

    fun onDoneOptionClick() {
        val currentOptionName = _uiState.value.optionName
        val currentOptionPrice = _uiState.value.priceForOption
        val newProductOptions = ProductOptions(
            optionsName = currentOptionName,
            priceForOptions = currentOptionPrice
        )
        _uiState.update {
            it.copy(
                listProductOptions = it.listProductOptions + newProductOptions,
                optionName = "",
                priceForOption = 0.0
            )
        }
    }

    fun onDeleteOptionClick(index: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                listProductOptions = currentState.listProductOptions.toMutableList().apply {
                    if (index in indices) removeAt(index)
                }
            )
        }
    }

    fun onEditOptionNameChange(index: Int, newName: String) {
        _uiState.update { currentState ->
            currentState.copy(
                listProductOptions = currentState.listProductOptions.toMutableList().apply {
                    if (index in indices) {
                        this[index] = this[index].copy(optionsName = newName)
                    }
                }
            )
        }
    }

    fun onEditOptionPriceChange(index: Int, price: String) {
        val newPrice = price.toDoubleOrNull() ?: 0.0
        _uiState.update { currentState ->
            currentState.copy(
                listProductOptions = currentState.listProductOptions.toMutableList().apply {
                    if (index in indices) {
                        this[index] = this[index].copy(priceForOptions = newPrice)
                    }
                }
            )
        }
    }

    fun onColorNameChange(colorName: String) {
        _uiState.update {
            it.copy(
                colorName = colorName
            )
        }
    }

    fun onImageColorUriChange(imageColor: Uri) {
        _uiState.update {
            it.copy(
                imageColorUri = imageColor
            )
        }
    }

    fun onDoneImageColorClick() {
        val currentColorName = _uiState.value.colorName
        val currentImageColorUri = _uiState.value.imageColorUri
        if (currentImageColorUri != null) { // Chỉ thêm nếu currentUri khác null
            val newColorOption = ColorOptions(
                colorName = currentColorName,
                imageColorUri = currentImageColorUri
            )
            _uiState.update {
                it.copy(
                    listColorOptions = it.listColorOptions + newColorOption,
                    colorName = "",
                    imageColorUri = null
                )
            }
            Log.d("AddProductViewModel", " listColorOptions : ${_uiState.value.listColorOptions}")
        }
    }

    fun onDeleteImageColorClick(index: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                listColorOptions = currentState.listColorOptions.toMutableList().apply {
                    if (index in indices) removeAt(index)
                }
            )
        }
    }

    fun onEditColorNameChange(index: Int, newName: String) {
        _uiState.update { currentState ->
            currentState.copy(
                listColorOptions = currentState.listColorOptions.toMutableList().apply {
                    if (index in indices) {
                        this[index] = this[index].copy(colorName = newName)
                    }
                }
            )
        }
    }

    fun onEditColorImageChange(index: Int, newImage: Uri) {
        _uiState.update { currentState ->
            currentState.copy(
                listColorOptions = currentState.listColorOptions.toMutableList().apply {
                    if (index in indices) {
                        this[index] = this[index].copy(imageColorUri = newImage)
                    }
                }
            )
        }
    }

    fun onColorByVariantChange(colorByVariant: String) {
        _uiState.update {
            it.copy(
                stockInputColor = colorByVariant
            )
        }
    }

    fun onOptionByVariantChange(optionByVariant: String) {
        _uiState.update {
            it.copy(
                stockInputOption = optionByVariant
            )
        }
    }

    fun onQuantityByVariantChange(quantityByVariant: String) {
        _uiState.update {
            it.copy(
                quantityStockInput = quantityByVariant
            )
        }
    }


    fun onDoneStockByVariantClick() {
        val newStockByVariant = StockByVariant(
            colorName = _uiState.value.stockInputColor,
            optionName = _uiState.value.stockInputOption,
            quantity = _uiState.value.quantityStockInput.toIntOrNull() ?: 0
        )
        _uiState.update {
            it.copy(
                listStockByVariant = it.listStockByVariant + newStockByVariant,
                stockInputColor = "",
                stockInputOption = "",
                quantityStockInput = ""
            )
        }
        Log.d("AddProductViewModel", "listStockByVariant: ${_uiState.value.listStockByVariant}")
    }

    fun onDeleteStockByVariantClick(index: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                listStockByVariant = currentState.listStockByVariant.toMutableList().apply {
                    if (index in indices) removeAt(index)
                }
            )
        }
    }

    fun onEditColorByVariantChange(index: Int, newName: String) {
        _uiState.update { currentState ->
            currentState.copy(
                listStockByVariant = currentState.listStockByVariant.toMutableList().apply {
                    if (index in indices) {
                        this[index] = this[index].copy(colorName = newName)
                    }
                }
            )
        }
    }

    fun onEditOptionByVariantChange(index: Int, newName: String) {
        _uiState.update { currentState ->
            currentState.copy(
                listStockByVariant = currentState.listStockByVariant.toMutableList().apply {
                    if (index in indices) {
                        this[index] = this[index].copy(optionName = newName)
                    }
                }
            )
        }
    }

    fun onEditQuantityStock(index: Int, newQuantity: String) {
        _uiState.update { currentState ->
            currentState.copy(
                listStockByVariant = currentState.listStockByVariant.toMutableList().apply {
                    if (index in indices) {
                        this[index] = this[index].copy(quantity = newQuantity.toIntOrNull() ?: 0)
                    }
                }
            )
        }
    }

    fun onDescriptionChange(description: String) {
        _uiState.update {
            it.copy(
                productDetailsItem = it.productDetailsItem.copy(
                    description = description
                )
            )
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
