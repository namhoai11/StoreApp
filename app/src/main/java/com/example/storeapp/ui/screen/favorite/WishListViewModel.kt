package com.example.storeapp.ui.screen.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import com.example.storeapp.model.UserModel
import com.example.storeapp.model.WishListModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WishListViewModel(
    private val repository: FirebaseFireStoreRepository

) : ViewModel() {
    private val _uiState = MutableStateFlow(WishListUiState())
    val uiState: StateFlow<WishListUiState> = _uiState

    private val _user = MutableLiveData<UserModel?>()
    val user: LiveData<UserModel?> = _user

    init {

        loadUser() // Đợi loadUser hoàn thành

        viewModelScope.launch {
            _uiState.collect { state ->
                Log.d("WishListViewmodel", "Current UI State: $state")
            }
        }


    }

    fun loadUser() {
        viewModelScope.launch {
            val userData = repository.getCurrentUser()
            _user.value = userData
            Log.d("WishListViewmodel", "User loaded: $userData")
            Log.d("WishListViewmodel", "User loaded, userValueWishList: ${_user.value?.wishList}")
            loadWishList()

        }
    }

    private fun loadWishList() = viewModelScope.launch {
        try {
//            _uiState.update { it.copy(showWishListLoading = true) }

            val listProductId = _user.value?.wishList ?: emptyList()
            Log.d("WishListViewModel", "listProductId: $listProductId")
            Log.d("WishListViewModel", "_user.value?.wishList: ${_user.value?.wishList}")

            val allProducts = repository.loadAllProducts()
            val wishListProduct = allProducts.filter { it.id in listProductId }
            Log.d("WishListViewModel", "wishListProduct: $wishListProduct")
            val allCategory = repository.loadCategory()
            val listWishListModel = emptyList<WishListModel>().toMutableList()
            wishListProduct.forEach { product ->
                val categoryItem = allCategory.find { it.id == product.categoryId.toIntOrNull() }
                val wishListItem = WishListModel(
                    productId = product.id,
                    productName = product.name,
                    productPrice = product.price,
                    productImage = product.images.firstOrNull() ?: "",
                    productCategory = categoryItem?.name ?: "",
                    productQuantity = product.stockQuantity
                )
                Log.d("WishListViewModel", "wishListItem: $wishListItem")

                listWishListModel += wishListItem

            }
            Log.d("WishListViewModel", "listItems: $listWishListModel")
            _uiState.update {
                it.copy(
                    listItems = listWishListModel,
                    showWishListLoading = false
                )
            }
        } catch (e: Exception) {
            // Xử lý lỗi nếu cần (thêm trạng thái lỗi vào UiState hoặc log)
            Log.e("WishListViewmodel", "Error loading wishlist: ${e.message}")
            // Xử lý lỗi chung, cập nhật trạng thái lỗi
            _uiState.update {
                it.copy(
                    showWishListLoading = false,
                    errorMessage = "Error loading wishlist: ${e.message}"
                )
            }
        }
    }

    fun favoriteClick(productId: String) {
        viewModelScope.launch {
            repository.removeWishListItem(productId)
            _uiState.update { currentState ->
                currentState.copy(
                    listItems = currentState.listItems.filterNot { it.productId == productId }
                )
            }
        }
    }
}