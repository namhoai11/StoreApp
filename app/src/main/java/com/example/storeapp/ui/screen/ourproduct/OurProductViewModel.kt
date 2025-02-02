package com.example.storeapp.ui.screen.ourproduct

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.StoreAppRepository
import com.example.storeapp.ui.uistate.OurProductUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OurProductViewModel(
    private val repository: StoreAppRepository
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
            _uiState.update { it.copy(showItemsLoading = true) }
            val allItems = repository.loadAllItems()
            Log.d("OurProductViewModel", "Our Product:$allItems")
            _uiState.update {
                it.copy(
                    allItems = allItems,
                    showItemsLoading = false
                )
            }

        } catch (e: Exception) {
            // Xử lý lỗi nếu cần (thêm trạng thái lỗi vào UiState hoặc log)
        }

    }

    // Hàm tìm kiếm sản phẩm theo tên
    fun searchItemsByName(query: String) = viewModelScope.launch {
        _uiState.update { currentState ->
            val filteredItems = if (query.isNotBlank()) {
                currentState.allItems.filter { it.name.contains(query, ignoreCase = true) }
            } else {
                currentState.allItems
            }
            currentState.copy(
                itemsSearched = filteredItems,
                currentQuery = query
            )
        }
        Log.d("OurProductViewModel", "itemsSearched:${_uiState.value.itemsSearched}")
    }
}