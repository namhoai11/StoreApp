package com.example.storeapp.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.StoreAppRepository
import com.example.storeapp.ui.uistate.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: StoreAppRepository
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
//            val banners = repository.loadBanner()
//            Log.d("HomeViewModel", "banners: $banners")
//            val categories = repository.loadCategory()
//            Log.d("HomeViewModel", "categories:$categories")
//            val allItems = repository.loadAllItems()
//            Log.d("HomeViewModel", "All Item:$allItems")
//            val itemsByCategory = allItems.groupBy { it.categoryId.toIntOrNull() ?: -1 }

            _uiState.update { it.copy(showBannerLoading = true) }
            val banners = repository.loadBanner()
            Log.d("HomeViewModel", "banners: $banners")
            _uiState.update { it.copy(banners = banners, showBannerLoading = false) }

            _uiState.update { it.copy(showCategoryLoading = true) }
            val categories = repository.loadCategory()
            Log.d("HomeViewModel", "categories:$categories")
            _uiState.update { it.copy(categories = categories, showCategoryLoading = false) }

//            _uiState.update { it.copy(showRecommenedLoading = true) }
//            val recommendedItems = repository.loadRecommended()
//            Log.d("HomeViewModel", "recommendedItems:$recommendedItems")
//            _uiState.update {
//                it.copy(
//                    recommendedItems = recommendedItems,
//                    showRecommenedLoading = false
//                )
//            }

            val allItems = repository.loadAllItems()
            Log.d("HomeViewModel", "All Item:$allItems")
            val itemsByCategory = allItems.groupBy { it.categoryId.toIntOrNull() ?: -2 }

            _uiState.update {
                it.copy(
                    allItems = allItems,
                    itemsByCategory = itemsByCategory,
//                    currentCategoryId = -1
                )
            }

            _uiState.update { it.copy(showRecommenedLoading = true) }
            val recommendedItems = allItems.filter { it.showRecommended == true }
            Log.d("HomeViewModel", "Recommended Items: $recommendedItems")
            _uiState.update {
                it.copy(
                    recommendedItems = recommendedItems,
                    showRecommenedLoading = false
                )
            }

        } catch (e: Exception) {
            // Xử lý lỗi nếu cần (thêm trạng thái lỗi vào UiState hoặc log)
        }
    }

    fun selectCategory(categoryId: Int) {
        _uiState.update { it.copy(currentCategoryId = categoryId) }
    }

    fun showAllItems() {
        _uiState.update { it.copy(currentCategoryId = -2) } // Hiển thị tất cả mục
    }

    fun showRecommendedItems() {
        _uiState.update { it.copy(currentCategoryId = -1) }
    }
}

