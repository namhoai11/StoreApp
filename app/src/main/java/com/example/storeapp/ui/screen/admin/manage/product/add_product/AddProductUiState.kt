package com.example.storeapp.ui.screen.admin.manage.product.add_product

import android.net.Uri
import com.example.storeapp.model.CategoryModel
import com.example.storeapp.model.ColorOptions
import com.example.storeapp.model.ProductModel
import com.example.storeapp.model.ProductOptions
import com.example.storeapp.model.StockByVariant

data class AddProductUiState(
    val productDetailsItem: ProductModel = ProductModel(),
    val listCategory: List<CategoryModel> = emptyList(),
    val categoryNameSelected: String = "",
    val priceInput: String = "",


    //image
    val listImageUriSelected: List<Uri> = emptyList(),
    val currentImageSelected: Uri? = null,

    //Loai đang duoc nhap
    val listProductOptions: List<ProductOptions> = emptyList(),
    val optionName: String = "",
    val priceForOption: Double = 0.0,
    //mau dang duoc nhap
    val listColorOptions: List<ColorOptions> = emptyList(),
    val colorName: String = "",
    val imageColorUri: Uri? = null,

//    stock được nhap
    val stockByVariant: List<StockByVariant> = emptyList(),
    val stockInputColor: String = "",
    val stockInputOption: String = "",
    val quantityStockInput: String = "",

    val isEditing: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String = "",
)