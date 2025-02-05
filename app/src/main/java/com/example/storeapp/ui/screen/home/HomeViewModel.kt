package com.example.storeapp.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.RealtimeDatabaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: RealtimeDatabaseRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadData()
//         Quan sát trạng thái của uiState
        viewModelScope.launch {
            _uiState.collect { state ->
                Log.d("HomeViewModel", "Current UI State: $state")
            }
        }
    }

    private fun loadData() = viewModelScope.launch {
        try {
            _uiState.update { it.copy(showBannerLoading = true) }
            val banners = repository.loadBanner()
            Log.d("HomeViewModel", "banners: $banners")
            _uiState.update { it.copy(banners = banners, showBannerLoading = false) }

            _uiState.update { it.copy(showCategoryLoading = true) }
            val categories = repository.loadCategory()
            Log.d("HomeViewModel", "categories:$categories")
            _uiState.update { it.copy(categories = categories, showCategoryLoading = false) }



            val allProducts = repository.loadAllProducts()
            Log.d("HomeViewModel", "All Product:$allProducts")
            val itemsByCategory = allProducts.groupBy { it.categoryId.toIntOrNull() ?: -2 }

            _uiState.update {
                it.copy(
                    allProducts = allProducts,
                    itemsByCategory = itemsByCategory,
//                    currentCategoryId = -1
                )
            }

            _uiState.update { it.copy(showRecommendedLoading = true) }
            val recommendedProducts = allProducts.filter { it.showRecommended }
            Log.d("HomeViewModel", "Recommended Products: $recommendedProducts")
            _uiState.update {
                it.copy(
                    recommendedProducts = recommendedProducts,
                    showRecommendedLoading = false
                )
            }

        } catch (e: Exception) {
            // Xử lý lỗi nếu cần (thêm trạng thái lỗi vào UiState hoặc log)
        }
    }

    fun selectCategory(categoryId: Int) {
        _uiState.update { it.copy(currentCategoryId = categoryId) }
    }
}

