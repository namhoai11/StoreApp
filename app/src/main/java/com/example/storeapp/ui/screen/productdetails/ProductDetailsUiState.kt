package com.example.storeapp.ui.screen.productdetails

import com.example.storeapp.model.ColorOptions
import com.example.storeapp.model.ProductModel
import com.example.storeapp.model.ProductOptions
import com.google.firebase.Timestamp

fun defaultItemsModel() = ProductModel(
    id = "1",
    name = "Business Laptop",
    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed  do eiusmod tempor incididunt ut labore et dolore magna  aliqua. Ut enim ad minim veniam, quis nostrud exercitation  ullamco laboris nisi ut aliquip ex ea commodo consequat.  Duis aute irure dolor in reprehenderit in voluptate velit esse  cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat  cupidatat non proident, sunt in culpa qui officia deserunt .Excepteur sint occaecat",
    images = arrayListOf(
        "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_1.png?alt=media&token=fb49a7c9-3094-4f5c-9ea6-b8365cd86323",
        "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_2.png?alt=media&token=3f826014-4808-4387-af6f-22dc7ddd4780",
        "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_3.png?alt=media&token=d4ab793a-cb72-45ab-ae43-8db69adaaeba",
        "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_4.png?alt=media&token=dfb10462-9138-471a-b34a-537bc7f5b7c8",
        "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_5.png?alt=media&token=2bfd17ef-d8c5-409e-8d6c-2d9e57d394c4"
    ),
    options = arrayListOf(
        "core i3",
        "core i5",
        "core i7"
    ),
    price = 550.0,
    rating = 4.7,
    categoryId = "0",
    hidden = false,
    stockQuantity = 12,
    createdAt = Timestamp.now(),
    updatedAt = Timestamp.now(),
)

data class ProductDetailsUiState(
    val productDetailsItem: ProductModel = defaultItemsModel(),
    val isWishListItem: Boolean = false,
    val currentImage: String = "",
    val currentPrice: Double = 0.0,
    val currentQuantity: Int = 0,
    val listStandardImages: List<String> = emptyList(),
    val listColorOptions: List<ColorOptions> = emptyList(),
    val listProductOptions: List<ProductOptions> = emptyList(),
    val selectedStandardImageUrl: Int = -1,
    val selectedColorUrl: Int = -1,
    val selectedOptionIndex: Int = -1,
    val showProductDetailsLoading: Boolean = true,
    val errorMessage: String = ""
)
