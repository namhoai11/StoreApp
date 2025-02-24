package com.example.storeapp.ui.screen.admin.manage.product

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductManagementViewModel(
    private val repository: FirebaseFireStoreRepository
):ViewModel() {
    private val _uiState = MutableStateFlow(ProductManagementUiState())
    val uiState: StateFlow<ProductManagementUiState> = _uiState


    init {
        loadData()
//         Quan sát trạng thái của uiState
        viewModelScope.launch {
            _uiState.collect { state ->
                Log.d("HomeViewModel", "Current UI State: $state")
            }
        }

    }

    fun loadData() = viewModelScope.launch {
        try {
            _uiState.update { it.copy(showCategoryLoading = true) }
            val categories = repository.loadCategory()
            Log.d("HomeViewModel", "categories:$categories")
            _uiState.update { it.copy(categories = categories, showCategoryLoading = false) }

            val allProducts = repository.loadAllProducts()
            Log.d("HomeViewModel", "All Product:$allProducts")
            val itemsByCategory = allProducts.groupBy { it.categoryId }

            _uiState.update {
                it.copy(
                    allProducts = allProducts,
                    itemsByCategory = itemsByCategory,
                )
            }
        } catch (e: Exception) {
            // Xử lý lỗi nếu cần (thêm trạng thái lỗi vào UiState hoặc log)
        }
    }

    fun selectCategory(categoryName: String) {
        val category = _uiState.value.categories.find { it.name==categoryName }
        if (category != null) {
            _uiState.update { it.copy(currentCategoryId = category.id) }
        }
    }
}