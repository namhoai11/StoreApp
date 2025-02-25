package com.example.storeapp.ui.screen.admin.manage.category.add_category

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class AddCategoryViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: FirebaseFireStoreRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddCategoryUiState())
    val uiState: StateFlow<AddCategoryUiState> = _uiState.asStateFlow()

    private val categoryId: String? = savedStateHandle["categoryId"]
    private val isEditing: Boolean = savedStateHandle["isEditing"] ?: false

    fun loadCategory() {
        viewModelScope.launch {
            if (categoryId.isNullOrEmpty()) {
                Log.w("AddCouponViewModel", "No categoryId provided, skipping load.")
                return@launch // Không cần load nếu không có categoryId
            }
            _uiState.update { it.copy(isLoading = true) }
            val result = repository.getCategoryById(categoryId)
            result.onSuccess { category ->
                _uiState.update {
                    it.copy(
                        categoryDetailsItem = category,
                        currentImageSelected = category.imageUrl.takeIf { imageUrl -> imageUrl.isNotEmpty() }
                            ?.let {
                                runCatching { Uri.parse(it) }.getOrNull()
                            },
                        isEditing = isEditing,
                        isLoading = false
                    )
                }
                Log.d("AddCategoryViewModel", "isEditing : $isEditing")

                Log.d(
                    "AddCategoryViewModel",
                    "Coupon Loaded: ${_uiState.value.categoryDetailsItem}"
                )
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
            }
        }
    }

    fun editCategoryClicked() {
        _uiState.update { it.copy(isEditing = true) }
    }

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(categoryDetailsItem = it.categoryDetailsItem.copy(name = newName)) }
    }

    fun onImageSelected(imageUri: Uri) {
        _uiState.update {
            it.copy(
                currentImageSelected = imageUri
            )
        }
    }

    fun onDescriptionChange(newDescription: String) {
        _uiState.update { it.copy(categoryDetailsItem = it.categoryDetailsItem.copy(description = newDescription)) }
    }

    private suspend fun uploadCategoryImage(): Boolean = withContext(Dispatchers.IO) {
        val categoryId =
            _uiState.value.categoryDetailsItem.id.ifBlank { UUID.randomUUID().toString() }

        // Kiểm tra xem có ảnh mới được chọn không
        val uploadedImageUrl = _uiState.value.currentImageSelected?.let { uri ->
            val result = repository.uploadImageToStorage(uri, "categories/$categoryId/category_image")
            result.getOrNull() // Lấy URL của ảnh sau khi upload
        } ?: _uiState.value.categoryDetailsItem.imageUrl // Nếu không có ảnh mới, giữ ảnh cũ

        // Cập nhật lại UI state với URL ảnh mới
        _uiState.update { currentState ->
            currentState.copy(
                categoryDetailsItem = currentState.categoryDetailsItem.copy(
                    id = categoryId,
                    imageUrl = uploadedImageUrl
                )
            )
        }
        return@withContext true
    }

    fun saveCategory() {
        _uiState.update { it.copy(isEditing = false) }
        if (!validateCategory()) {
            Log.e("SaveCategory", "Dữ liệu danh muc không hợp lệ:${_uiState.value.errorMessage}}")
            return
        }
        viewModelScope.launch {
            val success = withContext(Dispatchers.IO) { uploadCategoryImage() }
            if (!success) {
                Log.e("SaveCategory", "Lỗi khi tải ảnh lên Firebase Storage")
                return@launch
            }
            val category = _uiState.value.categoryDetailsItem
            Log.d("SaveCategory", "Dữ liệu danh muc trước khi lưu: $category")
            val result = repository.addOrUpdateCategoryToFireStore(category)
            result.onSuccess {
                _uiState.update {
                    it.copy(successMessage = "Thêm product thành công!", isEditing = false)
                }
                Log.d("AddCategoryUiState", "defaultUiState: ${_uiState.value}")
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
            }
        }
    }

    fun removeCategory(navigation: () -> Unit){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = "") }
            val categoryId = _uiState.value.categoryDetailsItem.id
            if (categoryId.isBlank()) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Category ID không hợp lệ"
                    )
                }
                return@launch
            }
            val result = repository.removeCategorytById(categoryId)
            result.onSuccess {
                navigation()

            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
            }
        }
    }

    private fun validateCategory(): Boolean {
        val product = _uiState.value.categoryDetailsItem
        if (product.name.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Tên danh mục không được để trống") }
            return false
        }
        if (product.description.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Mô tả danh mục không được để trống") }
            return false
        }
        if (_uiState.value.currentImageSelected == null) {
            _uiState.update { it.copy(errorMessage = "Vui lòng chọn ít nhất một ảnh danh mục") }
            return false
        }
        // Nếu tất cả đều hợp lệ, xóa thông báo lỗi
        _uiState.update { it.copy(errorMessage = null) }
        return true
    }
}