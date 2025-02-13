package com.example.storeapp.model

import com.google.firebase.Timestamp


data class ProductModel(
    val id: String = "",
    val name: String = "",
    val images: List<String> = emptyList(),
    val price: Double = 0.0,
    val stockQuantity: Int = 0,  // Tổng số lượng của tất cả biến thể
    val brandId: String? = null,
    val categoryId: String = "",
    val hidden: Boolean = false,
    var showRecommended: Boolean = false,
    val description: String = "",
    val rating: Double = 0.0,
    val availableOptions: AvailableOptions? = null,
    val options: List<String> = emptyList(),
    val stockByVariant: List<StockByVariant> = emptyList(), // 🔥 Thêm danh sách tồn kho theo biến thể
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now(),
    val brand: BrandModel? = null
) {
    constructor() : this(
        "",
        "",
        emptyList(),
        0.0,
        0,
        null,
        "",
        false,
        false,
        "",
        0.0,
        null,
        emptyList(),
        emptyList(),
        Timestamp.now(),
        Timestamp.now(),
        null
    )
    constructor(
        id: String,
        name: String,
        images: List<String>,
        price: Double,
        brandId: String?,
        categoryId: String,
        hidden: Boolean,
        showRecommended: Boolean,
        description: String,
        rating: Double,
        availableOptions: AvailableOptions?,
        options: List<String>,
        stockByVariant: List<StockByVariant>,
        createdAt: Timestamp,
        updatedAt: Timestamp,
        brand: BrandModel?
    ) : this(
        id,
        name,
        images,
        price,
        stockByVariant.sumOf { it.quantity }, // Tự động tính tổng số lượng
        brandId,
        categoryId,
        hidden,
        showRecommended,
        description,
        rating,
        availableOptions,
        options,
        stockByVariant,
        createdAt,
        updatedAt,
        brand
    )
}


data class StockByVariant(
    val colorName: String = "",
    val optionName: String = "",
    val quantity: Int = 0
) {
    constructor() : this("", "", 0)
    constructor(colorName: String, optionName: String) : this(colorName, optionName, 0)
}

data class AvailableOptions(
    val listProductOptions: List<ProductOptions> = emptyList(),
    val listColorOptions: List<ColorOptions> = emptyList()
) {
    constructor() : this(emptyList(), emptyList())
}

data class ProductOptions(
    val optionsName: String? = null,
    val priceForOptions: Double = 0.0
) {
    constructor() : this(null, 0.0)
    constructor(optionsName: String) : this(optionsName, 0.0)
}

data class ColorOptions(
    val colorName: String = "",
    val imagesColor: String = ""
) {
    constructor() : this("", "")
    constructor(colorName: String) : this(colorName, "")
}

data class Stock(
    val ProductId: String,
    val stockQuantity: Int,
    val stockByVariant: List<StockByVariant> = emptyList(),
){
    constructor() : this("", 0, emptyList()) // Constructor không tham số
}

