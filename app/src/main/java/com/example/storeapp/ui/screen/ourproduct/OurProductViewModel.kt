package com.example.storeapp.ui.screen.ourproduct

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.RealtimeDatabaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OurProductViewModel(
    private val repository: RealtimeDatabaseRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(OurProductUiState())
    val uiState: StateFlow<OurProductUiState> = _uiState

    init {
        loadDataOurProduct()
//         Quan sát trạng thái của uiState
        viewModelScope.launch {
            _uiState.collect { state ->
                Log.d("OurProductViewModel", "Current UI State: $state")
            }
        }
    }

    private fun loadDataOurProduct() = viewModelScope.launch {
        try {
            _uiState.update { it.copy(showProductsLoading = true) }
            val allProducts = repository.loadAllProducts()
            Log.d("OurProductViewModel", "Our Product:$allProducts")
            _uiState.update {
                it.copy(
                    allProducts = allProducts,
                    showProductsLoading = false
                )
            }

        } catch (e: Exception) {
            // Xử lý lỗi nếu cần (thêm trạng thái lỗi vào UiState hoặc log)
        }

    }

    // Hàm tìm kiếm sản phẩm theo tên
    fun searchItemsByName(query: String) = viewModelScope.launch {
        _uiState.update { currentState ->
            val filteredProducts = if (query.isNotBlank()) {
                currentState.allProducts.filter { it.name.contains(query, ignoreCase = true) }
            } else {
                currentState.allProducts
            }
            currentState.copy(
                productsSearched = filteredProducts,
                currentQuery = query
            )
        }
        Log.d("OurProductViewModel", "itemsSearched:${_uiState.value.productsSearched}")
    }
}