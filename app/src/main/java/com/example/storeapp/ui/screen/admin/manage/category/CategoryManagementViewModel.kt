package com.example.storeapp.ui.screen.admin.manage.category

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoryManagementViewModel(
    private val repository: FirebaseFireStoreRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CategoryManagementUiState())
    val uiState: StateFlow<CategoryManagementUiState> = _uiState

    init {
        loadData()
//         Quan sát trạng thái của uiState
        viewModelScope.launch {
            _uiState.collect { state ->
                Log.d("CategoryManagementViewModel", "Current UI State: $state")
            }
        }

    }

    fun loadData() = viewModelScope.launch {
        try {
            _uiState.update { it.copy(showCategoryLoading = true) }

            val categories = repository.loadCategory()
            Log.d("CategoryManagementViewModel", "categories: $categories")

            val allProducts = repository.loadAllProducts()
            Log.d("CategoryManagementViewModel", "All Products: $allProducts")

            // Tạo map đếm số sản phẩm trong mỗi category
            val productCountByCategory = allProducts.groupingBy { it.categoryId }.eachCount()
            Log.d("CategoryManagementViewModel", "Product Count by Category: $productCountByCategory")

            // Cập nhật danh sách categories với số lượng sản phẩm
            val updatedCategories = categories.map { category ->
                category.copy(productCount = productCountByCategory[category.id] ?: 0)
            }
            _uiState.update {
                it.copy(
                    categories = updatedCategories,
                    allProducts = allProducts,
                    showCategoryLoading = false
                )
            }

        } catch (e: Exception) {
            Log.e("CategoryManagementViewModel", "Error loading data", e)
        }
    }

    // Hàm tìm kiếm sản phẩm theo tên
    fun searchCategorysByName(query: String) = viewModelScope.launch {
        _uiState.update { currentState ->
            val filteredCategory = if (query.isNotBlank()) {
                currentState.categories.filter { it.name.contains(query, ignoreCase = true) }
            } else {
                currentState.categories
            }
            currentState.copy(
                categoriesSearched = filteredCategory,
                currentQuery = query
            )
        }
        Log.d("ProductManagementViewModel", "itemsSearched:${_uiState.value.categoriesSearched}")
    }
}