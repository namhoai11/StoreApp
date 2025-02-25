package com.example.storeapp.ui.screen.admin.manage.product.add_product

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import com.example.storeapp.model.AvailableOptions
import com.example.storeapp.model.ColorOptions
import com.example.storeapp.model.ProductModel
import com.example.storeapp.model.ProductOptions
import com.example.storeapp.model.StockByVariant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class AddProductViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: FirebaseFireStoreRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddProductUiState())
    val uiState: StateFlow<AddProductUiState> = _uiState.asStateFlow()

    private val productId: String? = savedStateHandle["productId"]
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

    fun loadProduct() {
        viewModelScope.launch {
            if (productId.isNullOrEmpty()) {
                Log.w("AddProductViewModel", "No productId provided, skipping load.")
                return@launch
            }
            _uiState.update { it.copy(isLoading = true) }

            val result = repository.getProductById(productId)

            result.onSuccess { product ->
                if (product != null) {
                    val categoryNameSelected = _uiState.value.listCategory.find { category ->
                        category.id == product.categoryId
                    }?.name ?: ""

                    val parsedColorOptions =
                        product.availableOptions?.listColorOptions?.map { colorOption ->
                            ColorOptionsForAddProduct(
                                colorName = colorOption.colorName,
                                imageColorUri = if (colorOption.imageColorUrl.isNotEmpty()) {
                                    runCatching { Uri.parse(colorOption.imageColorUrl) }.getOrNull()
                                } else null,
                                imageColorUrl = colorOption.imageColorUrl
                            )
                        } ?: emptyList()

                    _uiState.update {
                        it.copy(
                            productDetailsItem = product,
                            categoryNameSelected = categoryNameSelected,
                            priceInput = product.price.toString(),
                            listImageUriSelected = product.images.mapNotNull {
                                runCatching { Uri.parse(it) }.getOrNull()
                            },
                            listProductOptions = product.availableOptions?.listProductOptions
                                ?: emptyList(),
                            listColorOptions = parsedColorOptions,
                            listStockByVariant = product.stockByVariant,
                            isEditing = isEditing,
                            isLoading = false
                        )
                    }
                    Log.d(
                        "AddProductViewModel",
                        "Product Loaded: ${_uiState.value.productDetailsItem}"
                    )
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "Không tìm thấy sản phẩm.")
                    }
                    Log.e("AddProductViewModel", "Product not found: $productId")
                }
            }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Lỗi tải sản phẩm: ${exception.message}"
                        )
                    }
                    Log.e("AddProductViewModel", "Error loading product", exception)
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

    fun editProductClicked() {
        _uiState.update { it.copy(isEditing = true) }
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
                    categoryId = categoryIdSelected
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
            val newColorOptionsForAddProduct = ColorOptionsForAddProduct(
                colorName = currentColorName,
                imageColorUri = currentImageColorUri
            )
            _uiState.update {
                it.copy(
                    listColorOptions = it.listColorOptions + newColorOptionsForAddProduct,
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

    private suspend fun uploadAllImages(): Boolean = withContext(Dispatchers.IO) {
        val productId =
            _uiState.value.productDetailsItem.id.ifBlank { UUID.randomUUID().toString() }

        val uploadedImageUrls =
            _uiState.value.listImageUriSelected.mapIndexedNotNull { index, uri ->
                val result =
                    repository.uploadImageToStorage(uri, "products/$productId/image_$index")
                result.getOrNull()
            }

        val uploadedColorOptions = _uiState.value.listColorOptions.map { colorOption ->
            val imageUri = colorOption.imageColorUri ?: return@map ColorOptions(
                colorName = colorOption.colorName,
                imageColorUrl = colorOption.imageColorUrl // Nếu không có Uri mới, giữ nguyên URL cũ
            )

            val result = repository.uploadImageToStorage(
                imageUri,
                "products/$productId/color_${colorOption.colorName}"
            )

            if (result.isSuccess) {
                ColorOptions(
                    colorName = colorOption.colorName,
                    imageColorUrl = result.getOrNull() ?: ""
                )
            } else {
                Log.e("Upload", "Lỗi tải ảnh màu: ${result.exceptionOrNull()?.message}")
                ColorOptions(
                    colorName = colorOption.colorName,
                    imageColorUrl = colorOption.imageColorUrl
                )
            }
        }
        _uiState.update { currentState ->
            currentState.copy(
                productDetailsItem = currentState.productDetailsItem.copy(
                    id = productId,
                    images = uploadedImageUrls,
                    availableOptions = AvailableOptions(
                        listProductOptions = _uiState.value.listProductOptions,
                        listColorOptions = uploadedColorOptions
                    ),
                    stockByVariant = _uiState.value.listStockByVariant
                ),
                // Cập nhật lại UI state
                listColorOptions = uploadedColorOptions.map {
                    ColorOptionsForAddProduct(
                        colorName = it.colorName,
                        imageColorUrl = it.imageColorUrl
                    )
                }
            )
        }
        return@withContext true
    }


    fun saveProduct() {
        _uiState.update { it.copy(isEditing = false) }

        if (!validateProduct()) {
            Log.e("SaveProduct", "Dữ liệu sản phẩm không hợp lệ")
            Log.e("SaveProduct", "Dữ liệu sản phẩm không hợp lệ:${_uiState.value.errorMessage}}")

            return
        }

        viewModelScope.launch {
            val success = withContext(Dispatchers.IO) { uploadAllImages() }
            if (!success) {
                Log.e("SaveProduct", "Lỗi khi tải ảnh lên Firebase Storage")
                return@launch
            }
            val product = _uiState.value.productDetailsItem

            Log.d("SaveProduct", "Dữ liệu sản phẩm trước khi lưu: $product")

            val result = repository.addOrUpdateProductToFireStore(product)

            result.onSuccess {
                _uiState.update {
                    it.copy(successMessage = "Thêm product thành công!", isEditing = false)
                }
                Log.d("AddProductUiState", "defaultUiState: ${_uiState.value}")
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
            }
        }
    }


    fun removeProduct(navigation: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = "") }
            val productId = _uiState.value.productDetailsItem.id
            if (productId.isBlank()) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Product ID không hợp lệ"
                    )
                }
                return@launch
            }
            val result = repository.removeProductById(productId)
            result.onSuccess {
                navigation()

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

        if (product.categoryId.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Vui lòng chọn danh mục sản phẩm") }
            return false
        }

        val price = _uiState.value.priceInput.toDoubleOrNull()
        if (price == null || price <= 0) {
            _uiState.update { it.copy(errorMessage = "Giá sản phẩm phải lớn hơn 0") }
            return false
        }

        if (product.description.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Mô tả sản phẩm không được để trống") }
            return false
        }

        if (_uiState.value.listImageUriSelected.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Vui lòng chọn ít nhất một ảnh sản phẩm") }
            return false
        }

        if (_uiState.value.listProductOptions.isEmpty() && _uiState.value.listColorOptions.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Sản phẩm cần ít nhất một tùy chọn hoặc màu sắc") }
            return false
        }

        if (_uiState.value.listStockByVariant.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Vui lòng nhập số lượng tồn kho") }
            return false
        }

        // Nếu tất cả đều hợp lệ, xóa thông báo lỗi
        _uiState.update { it.copy(errorMessage = null) }
        return true
    }

}
