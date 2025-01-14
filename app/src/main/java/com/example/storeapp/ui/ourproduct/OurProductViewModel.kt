package com.example.storeapp.ui.ourproduct

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
            _uiState.update { it.copy(
                allItems = allItems,
                showItemsLoading = false
            ) }

        }catch (e: Exception) {
            // Xử lý lỗi nếu cần (thêm trạng thái lỗi vào UiState hoặc log)
        }

    }
}