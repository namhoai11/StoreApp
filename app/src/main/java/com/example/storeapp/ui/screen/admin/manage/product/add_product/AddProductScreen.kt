package com.example.storeapp.ui.screen.admin.manage.product.add_product

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.storeapp.R
import com.example.storeapp.data.local.DataDummy
import com.example.storeapp.model.CategoryModel
import com.example.storeapp.model.ColorOptions
import com.example.storeapp.model.ProductOptions
import com.example.storeapp.model.StockByVariant
import com.example.storeapp.ui.component.admin.AddProductTextField
import com.example.storeapp.ui.component.admin.AddValueLargeTextField
import com.example.storeapp.ui.component.admin.AddValueTextField
import com.example.storeapp.ui.component.admin.ColorImagePicker
import com.example.storeapp.ui.component.admin.FilterOption
import com.example.storeapp.ui.component.admin.IconButton
import com.example.storeapp.ui.component.admin.ImagePicker
import com.example.storeapp.ui.component.function.formatCurrency2
import com.example.storeapp.ui.screen.admin.manage.coupon.add_coupon.AddTextField
import com.example.storeapp.ui.theme.StoreAppTheme
import com.google.firebase.Timestamp


@Composable
fun AddProductScreen() {

}

@Composable
fun AddProductContent(
    innerPadding: PaddingValues,
    uiState: AddProductUiState,
    isEditing: Boolean,
    editProductClick: () -> Unit = {},
    deleteProductClick: () -> Unit = {},
    onNameChange: (String) -> Unit = {},
//    onQuantityChange: (String) -> Unit = {},
    onCategoryNameSelected: (String) -> Unit = {},
    onPriceChange: (String) -> Unit = {},

    onDoneImageClick: () -> Unit = {},
    onDeleteImageClick: (Int) -> Unit = {},
    onImageSelected: (Uri) -> Unit = { },
    onEditImageChange: (Int, Uri) -> Unit = { _, _ -> },

    onDoneOptionClick: () -> Unit = {},
    onDeleteOptionClick: (Int) -> Unit = {},
    onOptionNameChange: (String) -> Unit = {},
    onOptionPriceChange: (String) -> Unit = { },
    onEditOptionNameChange: (Int, String) -> Unit = { _, _ -> },
    onEditOptionPriceChange: (Int, Double) -> Unit = { _, _ -> },

    onEditColorNameChange: (Int, String) -> Unit = { _, _ -> },
    onEditImageColorUriChange: (Int, Uri) -> Unit = { _, _ -> },
    onDoneColorClick: () -> Unit = {},
    onDeleteColorClick: (Int) -> Unit = {},
    onColorNameChange: (String) -> Unit = {},
    onImageColorUriChange: (Uri) -> Unit = { },

    onEditColorByVariant: (Int, String) -> Unit = { _, _ -> },
    onEditOptionByVariant: (Int, String) -> Unit = { _, _ -> },
    onEditQuantityByVariantChange: (Int, Int) -> Unit = { _, _ -> },
    onColorByVariantChange: (String) -> Unit = {},
    onOptionByVariantChange: (String) -> Unit = {},
    onQuantityByVariantChange: (String) -> Unit = { },
    onDoneStockByVariantClick: () -> Unit = {},
    onDeleteStockByVariantClick: (Int) -> Unit = {},

    onDescriptionChange: (String) -> Unit = {},
    onConfirm: () -> Unit = {} // Hàm xử lý khi bấm "Xác nhận"
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        Column(
            modifier = Modifier
                .weight(1f) // Dùng weight để phần nội dung có thể cuộn được mà không che button
                .padding(8.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sản phẩm",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.weight(1f))
                if (!isEditing) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(
                            Color.Cyan,
                            R.drawable.edit,
                            "Edit",
                            editProductClick
                        )
                        IconButton(
                            Color.Red,
                            R.drawable.icon_trash,
                            "Delete",
                            deleteProductClick
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circle at the bottom
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .background(Color.Gray, shape = CircleShape)
//                        .align(Alignment.BottomStart) // Đặt chấm tròn ở dưới cùng
                )
                Text(
                    text = "Id",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = uiState.productDetailsItem.id,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(8.dp)
                )

            }
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(16.dp)
            )

            AddTextField(
                title = "Tên",
                isEditing = isEditing,
                valueInput = uiState.productDetailsItem.name,
                onValueChange = onNameChange,
            )
            AddCategoryField(
                title = "Loại",
                isEditing = isEditing,
                listCategory = uiState.listCategory,
                selectedCategory = uiState.categoryNameSelected,
                onCategorySelected = onCategoryNameSelected
            )
            AddProductTextField(
                title = "Giá",
                isEditing = isEditing,
                valueInput = uiState.priceInput,
                onValueChange = onPriceChange,
                isNumberInput = true,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Circle at the bottom
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .background(Color.Gray, shape = CircleShape)
//                        .align(Alignment.BottomStart) // Đặt chấm tròn ở dưới cùng
                )
                Text(
                    text = "Ảnh",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                val isColorEditingList =
                    remember { mutableStateListOf(*Array(uiState.listImageUriSelected.size) { false }) }
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    uiState.listImageUriSelected.forEachIndexed { index, image ->
                        ImagePicker(
                            isEditing = isColorEditingList[index],
                            imageColorUri = image,
                            onImageSelected = {
                                onEditImageChange(
                                    index,
                                    it
                                )
                            },
                            onEditColorClick = {
                                isColorEditingList[index] = true
                            },
                            onDeleteColorClick = { onDeleteImageClick(index) },
                            onDoneColorClick = {
                                isColorEditingList[index] = false
                            }
                        )
                    }
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(16.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Circle at the bottom
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .background(Color.Gray, shape = CircleShape)
//                        .align(Alignment.BottomStart) // Đặt chấm tròn ở dưới cùng
                    )
                    Text(
                        text = "Thêm ảnh",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                ImagePicker(
                    isEditing = isEditing,
                    imageColorUri = uiState.imageColorUri,
                    onImageSelected = {
                        onImageSelected(it)
                    },
                    onDoneColorClick = {
                        onDoneImageClick()
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circle at the bottom
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .background(Color.Gray, shape = CircleShape)
//                        .align(Alignment.BottomStart) // Đặt chấm tròn ở dưới cùng
                )
                Text(
                    text = "Phân loại",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }

            val isEditingList =
                remember { mutableStateListOf(*Array(uiState.listProductOptions.size) { false }) }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                uiState.listProductOptions.forEachIndexed { index, productOption ->
                    AddOptionTextField(
                        isEditing = isEditingList[index],
                        title = "Loại",
                        title2 = "Giá",
                        optionName = productOption.optionsName,
                        optionPrice = productOption.priceForOptions,
                        onOptionNameChange = {
                            onEditOptionNameChange(
                                index,
                                it
                            )
                        },
                        onOptionPriceChange = {
                            onEditOptionPriceChange(
                                index,
                                it.toDouble()
                            )
                        },
                        onEditOptionClick = {
                            isEditingList[index] = true
                        },
                        onDeleteOptionClick = { onDeleteOptionClick(index) },
                        onDoneOptionClick = {
                            isEditingList[index] = false
                        }
                    )
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(16.dp)
            )

            AddOptionTextField(
                isEditing = isEditing,
                title = "Loại",
                title2 = "Giá",
                optionName = uiState.optionName,
                optionPrice = uiState.priceForOption,
                onOptionNameChange = { onOptionNameChange(it) },
                onOptionPriceChange = { onOptionPriceChange(it) },
                onDoneOptionClick = onDoneOptionClick
            )
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(16.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circle at the bottom
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .background(Color.Gray, shape = CircleShape)
//                        .align(Alignment.BottomStart) // Đặt chấm tròn ở dưới cùng
                )
                Text(
                    text = "Màu sắc",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
            val isColorOptionEditingList =
                remember { mutableStateListOf(*Array(uiState.listColorOptions.size) { false }) }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                uiState.listColorOptions.forEachIndexed { index, colorOption ->
                    ColorImagePicker(
                        isEditing = isColorOptionEditingList[index],
                        title = "Màu",
                        colorName = colorOption.colorName,
                        imageColorUri = colorOption.imageColorUri,
                        onColorNameChange = {
                            onEditColorNameChange(
                                index,
                                it
                            )
                        },
                        onImageSelected = {
                            onEditImageColorUriChange(
                                index,
                                it
                            )
                        },
                        onEditColorClick = {
                            isColorOptionEditingList[index] = true
                        },
                        onDeleteColorClick = { onDeleteColorClick(index) },
                        onDoneColorClick = {
                            isColorOptionEditingList[index] = false
                        }
                    )
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(16.dp)
            )
            ColorImagePicker(
                isEditing = isEditing,
                title = "Màu",
                colorName = uiState.colorName,
                imageColorUri = uiState.imageColorUri,
                onColorNameChange = { onColorNameChange(it) },
                onImageSelected = { onImageColorUriChange(it) },
                onDoneColorClick = { onDoneColorClick() }
            )
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(16.dp)
            )
            Column {
                // Circle at the bottom
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .background(Color.Gray, shape = CircleShape)
//                        .align(Alignment.BottomStart) // Đặt chấm tròn ở dưới cùng
                    )
                    Text(
                        text = "Số lượng",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                val isStockByVariantList =
                    remember { mutableStateListOf(*Array(uiState.stockByVariant.size) { false }) }
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    uiState.stockByVariant.forEachIndexed { index, variant ->
                        AddStockByVariantField(
                            isEditing = isStockByVariantList[index],
                            listColorOptions = uiState.listColorOptions,
                            selectedColor = variant.colorName,
                            onColorSelected = {
                                onEditColorByVariant(index, it)
                            },
                            listProductOptions = uiState.listProductOptions,
                            selectedOption = variant.optionName,
                            onOptionSelected = {
                                onEditOptionByVariant(index, it)
                            },
                            stockByVariant = uiState.stockByVariant,
                            quantity = variant.quantity.toString(),
                            onQuantityChange = {
                                onEditQuantityByVariantChange(index, it.toInt())
                            },
                            onEditStockByVariantClick = {
                                isStockByVariantList[index] = true
                            },
                            onDeleteStockByVariantClick = { onDeleteStockByVariantClick(index) },
                            onDoneStockByVariantClick = {
                                isStockByVariantList[index] = false
                            }
                        )
                    }
                }

            }

            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(16.dp)
            )
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(16.dp)
            )

//            onColorByVariantChange: (String) -> Unit = {},
//            onOptionByVariantChange: (String) -> Unit = {},
//            onQuantityByVariantChange: (Int) -> Unit = { },
            Column {
                // Circle at the bottom
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .background(Color.Gray, shape = CircleShape)
//                        .align(Alignment.BottomStart) // Đặt chấm tròn ở dưới cùng
                    )
                    Text(
                        text = "Nhập Số lượng",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                AddStockByVariantField(
                    isEditing = isEditing,
                    listColorOptions = uiState.listColorOptions,
                    selectedColor = uiState.stockInputColor,
                    onColorSelected = {
                        onColorByVariantChange(it)
                    },
                    listProductOptions = uiState.listProductOptions,
                    selectedOption = uiState.stockInputOption,
                    onOptionSelected = {
                        onOptionByVariantChange(it)
                    },
                    stockByVariant = uiState.stockByVariant,
                    quantity = uiState.quantityStockInput,
                    onQuantityChange = {
                        onQuantityByVariantChange(it)
                    },
//                    onEditStockByVariantClick = {
//                        isStockByVariantList[index] = true
//                    },
//                    onDeleteStockByVariantClick = { onDeleteColorClick(index) },
                    onDoneStockByVariantClick = {
                        onDoneStockByVariantClick()
                    }
                )
            }
            AddValueLargeTextField(
                title = "Mô tả",
                isEditing = isEditing,
                valueInput = uiState.productDetailsItem.description,
                onValueChange = onDescriptionChange
            )
        }
        if (isEditing) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(color = Color.White),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    modifier = Modifier
                        .height(87.dp)
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(10.dp),
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(text = "Xác nhận", fontSize = 14.sp, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun AddCategoryField(
    title: String,
    isEditing: Boolean,
    listCategory: List<CategoryModel>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val listCategoryName = listCategory.map { it.name }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Circle at the bottom
        Box(
            modifier = Modifier
                .size(18.dp)
                .background(Color.Gray, shape = CircleShape)
//                        .align(Alignment.BottomStart) // Đặt chấm tròn ở dưới cùng
        )
        Text(
            text = title,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(8.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        FilterOption(
            modifier = Modifier.width(250.dp),
            isEditing = isEditing,
            listOptions = listCategoryName,
            selectedOption = selectedCategory,
            onOptionSelected = onCategorySelected
        )
    }
    HorizontalDivider(
        thickness = 1.dp,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun AddStockByVariantField(
    isEditing: Boolean,
    listColorOptions: List<ColorOptions> = emptyList(),
    selectedColor: String = "",
    onColorSelected: (String) -> Unit = {},
    listProductOptions: List<ProductOptions> = emptyList(),
    selectedOption: String = "",
    onOptionSelected: (String) -> Unit = {},
    stockByVariant: List<StockByVariant> = emptyList(),
    quantity: String = "",
    onQuantityChange: (String) -> Unit = {},
    onDoneStockByVariantClick: () -> Unit = {},
    onEditStockByVariantClick: () -> Unit = {},
    onDeleteStockByVariantClick: () -> Unit = {},
) {
    // Lấy danh sách màu
    val availableColors = listColorOptions.map { it.colorName }
    // Lọc danh sách Option dựa trên màu đã chọn
    val usedOptions = stockByVariant.filter { it.colorName == selectedColor }.map { it.optionName }
    val availableOptions = listProductOptions.map { it.optionsName }.filter { it !in usedOptions }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FilterOption(
                modifier = Modifier.weight(1f),
                isEditing = isEditing,
                listOptions = availableColors,
                selectedOption = selectedColor,
                onOptionSelected = onColorSelected
            )
            FilterOption(
                modifier = Modifier.weight(1f),
                isEditing = isEditing,
                listOptions = availableOptions,
                selectedOption = selectedOption,
                onOptionSelected = onOptionSelected
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AddValueTextField(
                modifier = Modifier.width(150.dp),
                title = "Số lượng",
                isEditing = isEditing,
                valueInput = quantity,
                onValueChange = { onQuantityChange(it) }
            )
            if (!isEditing) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        Color.Cyan,
                        R.drawable.edit,
                        "Edit",
                        onEditStockByVariantClick
                    )
                    IconButton(
                        Color.Red,
                        R.drawable.icon_trash,
                        "Delete",
                        onDeleteStockByVariantClick
                    )
                }
            } else {
                IconButton(
                    Color.Green,
                    R.drawable.icon_check_mark,
                    "Done",
                    onDoneStockByVariantClick
                )
            }

        }
    }
    HorizontalDivider(
        thickness = 1.dp,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun AddOptionTextField(
    isEditing: Boolean,
    title: String,
    title2: String,
    optionName: String = "",
    optionPrice: Double = 0.0,
    onOptionNameChange: (String) -> Unit,
    onOptionPriceChange: (String) -> Unit,
    onDoneOptionClick: () -> Unit = {},
    onEditOptionClick: () -> Unit = {},
    onDeleteOptionClick: () -> Unit = {},
    isNumberInput: Boolean = false
) {
    val optionPriceToString = formatCurrency2(optionPrice)
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AddValueTextField(
            modifier = Modifier.weight(1f),
            title = title,
            isEditing = isEditing,
            valueInput = optionName,
            onValueChange = onOptionNameChange,
            isNumberInput = isNumberInput
        )
        AddValueTextField(
            modifier = Modifier.weight(1f),
            title = title2,
            isEditing = isEditing,
            valueInput = optionPriceToString,
            onValueChange = onOptionPriceChange,
            isNumberInput = !isNumberInput
        )
        if (!isEditing) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    contentAlignment = Alignment.Center, // Căn giữa nội dung trong Box
                    modifier = Modifier
                        .padding(2.dp)
                        .size(32.dp)
                        .background(
                            color = Color.Cyan,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            onEditOptionClick()
                        }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.edit),
                        contentDescription = "Edit Icon",
                        tint = Color.White, // Đặt màu icon thành đen
                        modifier = Modifier.size(20.dp)
                    )
                }
                Box(
                    contentAlignment = Alignment.Center, // Căn giữa nội dung trong Box
                    modifier = Modifier
                        .padding(2.dp)
                        .size(32.dp)
                        .background(
                            color = Color.Red,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            onDeleteOptionClick()
                        }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_trash),
                        contentDescription = "Trash Icon",
                        tint = Color.White, // Đặt màu icon thành đen
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        } else {
            Box(
                contentAlignment = Alignment.Center, // Căn giữa nội dung trong Box
                modifier = Modifier
                    .padding(2.dp)
                    .size(32.dp)
                    .background(
                        color = Color.Green,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable {
                        onDoneOptionClick()
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_check_mark),
                    contentDescription = "Trash Icon",
                    tint = Color.White, // Đặt màu icon thành đen
                    modifier = Modifier.size(20.dp)
                )
            }
        }

    }
}

@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAddProductContent() {
    StoreAppTheme {
        AddProductContent(
            innerPadding = PaddingValues(0.dp),
            uiState = DataDummy.addProductUiState,
            isEditing = true
        )
    }
}


@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AddCategoryFieldPreview() {
    StoreAppTheme {
        var selectedCategory by remember { mutableStateOf("selectedCategory.name") }
//        val listCategory = DataDummy.categoryList.map { it.name }
        val category = CategoryModel(
            id = 0,
            name = "Pc",
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat1.png?alt=media&token=e3988db7-b935-495a-abbb-89a1b0aa5e0e",
            description = "",
            hidden = false,
            productCount = 1,
            createdAt = Timestamp.now(),
            updatedAt = Timestamp.now(),
        )
        AddCategoryField(
            title = "Danh muc",
            isEditing = true,
            listCategory = DataDummy.categoryList,
            selectedCategory = category.name,
            onCategorySelected = { selectedCategory = it })
    }
}

@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AddStockByVariantFieldPreview() {
    StoreAppTheme {
        var selectedCategory by remember { mutableStateOf("selectedCategory.name") }
        val category = CategoryModel(
            id = 0,
            name = "Pc",
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat1.png?alt=media&token=e3988db7-b935-495a-abbb-89a1b0aa5e0e",
            description = "",
            hidden = false,
            productCount = 1,
            createdAt = Timestamp.now(),
            updatedAt = Timestamp.now(),
        )
        AddStockByVariantField(
            isEditing = true,

            )
    }
}


@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAddOptionTextField() {
    StoreAppTheme {
        AddOptionTextField(
            title = "Loai",
            title2 = "Gia",
            optionName = "core i7",
            optionPrice = 150000.0,
            isEditing = false,
            onOptionNameChange = {},
            onOptionPriceChange = {},
        )
    }
}

@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAddOptionTextField2() {
    StoreAppTheme {
        AddOptionTextField(
            title = "Loai",
            title2 = "Gia",
            optionName = "core i7",
            optionPrice = 150000.0,
            isEditing = true,
            onOptionNameChange = {},
            onOptionPriceChange = {},
        )
    }
}
