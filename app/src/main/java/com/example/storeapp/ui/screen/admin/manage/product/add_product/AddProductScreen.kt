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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.storeapp.R
import com.example.storeapp.data.local.DataDummy
import com.example.storeapp.model.CategoryModel
import com.example.storeapp.model.ProductOptions
import com.example.storeapp.model.StockByVariant
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.admin.AddProductTextField
import com.example.storeapp.ui.component.admin.AddValueLargeTextField
import com.example.storeapp.ui.component.admin.AddValueTextField
import com.example.storeapp.ui.component.admin.AdminTopAppBar
import com.example.storeapp.ui.component.admin.ColorImagePicker
import com.example.storeapp.ui.component.admin.FilterOption
import com.example.storeapp.ui.component.admin.IconButton
import com.example.storeapp.ui.component.admin.ImagePicker
import com.example.storeapp.ui.component.function.formatCurrency2
import com.example.storeapp.ui.component.function.formatNumber
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.screen.admin.manage.coupon.add_coupon.AddTextField
import com.example.storeapp.ui.theme.StoreAppTheme
import com.google.firebase.Timestamp


//object AddProductManagementDestination : NavigationDestination {
//    override val route = "addproductmanagement/?productId={productId}&isEditing={isEditing}"
//    override val titleRes = R.string.addcouponmanage_title
//
//    fun createRoute(productId: String?, isEditing: Boolean): String {
//        return if (productId == null) {
//            "addproductmanagement/?isEditing=$isEditing"  // Giữ format nhất quán
//        } else {
//            "addproductmanagement/?productId=$productId&isEditing=$isEditing"
//        }
//    }
//}
object AddProductManagementDestination : NavigationDestination {
    override val route = "addproductmanagement?productId={productId}&isEditing={isEditing}"
    override val titleRes = R.string.addcouponmanage_title

    fun createRoute(productId: String?, isEditing: Boolean): String {
        return if (productId == null) {
            "addproductmanagement?isEditing=$isEditing"
        } else {
            "addproductmanagement?productId=$productId&isEditing=$isEditing"
        }
    }
}

@Composable
fun AddProductScreen(
    navController: NavController,
    viewModel: AddProductViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by viewModel.uiState.collectAsState()
    val productId = uiState.productDetailsItem.id
    val isEditing = uiState.isEditing
    val textRole = when {
        productId == "" && isEditing -> {
            "Thêm"
        }

        productId != "" && isEditing -> {
            "Sửa"
        }

        else -> {
            "Chi tiết"
        }
    }
    LaunchedEffect(Unit) {
        viewModel.loadProduct()
    }
    Scaffold(
        topBar = {
            AdminTopAppBar(
                R.drawable.arrowback,
                textRole,
                "Sản phẩm",
                { navController.navigateUp() },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 48.dp)
            )
        },
    ) { innerPadding ->
        AddProductContent(
            innerPadding = innerPadding,
            uiState = uiState,
            isEditing = isEditing,
            editProductClick = { viewModel.editProductClicked() },
            deleteProductClick = {
                viewModel.removeProduct { navController.navigateUp() }
            },
            onNameChange = { viewModel.onNameChange(it) },
            onCategoryNameSelected = { viewModel.onCategoryNameSelected(it) },
            onPriceChange = { viewModel.onPriceChange(it) },

            onImageSelected = { viewModel.onImageSelected(it) },
            onDoneImageClick = { viewModel.onDoneImageClick() },
            onDeleteImageClick = { viewModel.onDeleteImageClick(it) },
            onEditImageChange = { index, uri ->
                viewModel.onEditImageChange(index, uri)
            },

            onOptionNameChange = { viewModel.onOptionNameChange(it) },
            onOptionPriceChange = { viewModel.onOptionPriceChange(it) },
            onDoneOptionClick = { viewModel.onDoneOptionClick() },
            onDeleteOptionClick = { viewModel.onDeleteOptionClick(it) },
            onEditOptionNameChange = { index, optionName ->
                viewModel.onEditOptionNameChange(index, optionName)
            },
            onEditOptionPriceChange = { index, optionPrice ->
                viewModel.onEditOptionPriceChange(index, optionPrice)
            },

            onColorNameChange = { viewModel.onColorNameChange(it) },
            onImageColorUriChange = { viewModel.onImageColorUriChange(it) },
            onDoneColorClick = { viewModel.onDoneImageColorClick() },
            onDeleteColorClick = { viewModel.onDeleteImageColorClick(it) },
            onEditColorNameChange = { index, colorName ->
                viewModel.onEditColorNameChange(index, colorName)
            },
            onEditImageColorUriChange = { index, uri ->
                viewModel.onEditColorImageChange(index, uri)
            },

            onColorByVariantChange = { viewModel.onColorByVariantChange(it) },
            onOptionByVariantChange = { viewModel.onOptionByVariantChange(it) },
            onQuantityByVariantChange = { viewModel.onQuantityByVariantChange(it) },
            onDoneStockByVariantClick = { viewModel.onDoneStockByVariantClick() },
            onDeleteStockByVariantClick = { viewModel.onDeleteStockByVariantClick(it) },
            onEditColorByVariant = { index, colorByVariantName ->
                viewModel.onEditColorByVariantChange(index, colorByVariantName)
            },
            onEditOptionByVariant = { index, optionByVariantName ->
                viewModel.onEditOptionByVariantChange(index, optionByVariantName)
            },
            onEditQuantityByVariantChange = { index, quantityByVariantName ->
                viewModel.onEditQuantityStock(index, quantityByVariantName)
            },

            onDescriptionChange = { viewModel.onDescriptionChange(it) },
            onConfirm = { viewModel.saveProduct() }


        )

    }

}

@Composable
fun AddProductContent(
    innerPadding: PaddingValues,
    uiState: AddProductUiState,
    isEditing: Boolean,
    editProductClick: () -> Unit = {},
    deleteProductClick: () -> Unit = {},
    onNameChange: (String) -> Unit = {},
    onCategoryNameSelected: (String) -> Unit = {},
    onPriceChange: (String) -> Unit = {},

    onImageSelected: (Uri) -> Unit = { },
    onDoneImageClick: () -> Unit = {},
    onDeleteImageClick: (Int) -> Unit = {},
    onEditImageChange: (Int, Uri) -> Unit = { _, _ -> },

    onOptionNameChange: (String) -> Unit = {},
    onOptionPriceChange: (String) -> Unit = {},
    onDoneOptionClick: () -> Unit = {},
    onDeleteOptionClick: (Int) -> Unit = {},
    onEditOptionNameChange: (Int, String) -> Unit = { _, _ -> },
    onEditOptionPriceChange: (Int, String) -> Unit = { _, _ -> },

    onColorNameChange: (String) -> Unit = {},
    onImageColorUriChange: (Uri) -> Unit = { },
    onEditColorNameChange: (Int, String) -> Unit = { _, _ -> },
    onEditImageColorUriChange: (Int, Uri) -> Unit = { _, _ -> },
    onDoneColorClick: () -> Unit = {},
    onDeleteColorClick: (Int) -> Unit = {},


    onColorByVariantChange: (String) -> Unit = {},
    onOptionByVariantChange: (String) -> Unit = {},
    onQuantityByVariantChange: (String) -> Unit = {},
    onDoneStockByVariantClick: () -> Unit = {},
    onDeleteStockByVariantClick: (Int) -> Unit = {},
    onEditColorByVariant: (Int, String) -> Unit = { _, _ -> },
    onEditOptionByVariant: (Int, String) -> Unit = { _, _ -> },
    onEditQuantityByVariantChange: (Int, String) -> Unit = { _, _ -> },

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
//                val isColorEditingList =
//                    remember { mutableStateListOf(*Array(uiState.listImageUriSelected.size) { false }) }

//                val isColorEditingList = remember { mutableStateListOf<Boolean>() }
//
//                LaunchedEffect(uiState.listImageUriSelected) {
//                    isColorEditingList.clear()
//                    isColorEditingList.addAll(List(uiState.listImageUriSelected.size) { false })
//                }
                val isColorEditingList = remember {
                    mutableStateListOf<Boolean>().apply {
                        repeat(uiState.listImageUriSelected.size) { add(false) }
                    }
                }

// Khi danh sách thay đổi kích thước, cập nhật danh sách isEditingList mà không mất trạng thái
                LaunchedEffect(uiState.listImageUriSelected.size) {
                    if (isColorEditingList.size < uiState.listImageUriSelected.size) {
                        repeat(uiState.listImageUriSelected.size - isColorEditingList.size) {
                            isColorEditingList.add(false)
                        }
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    uiState.listImageUriSelected.forEachIndexed { index, image ->
                        ImagePicker(
                            isEditing = isColorEditingList.getOrElse(index) { false },
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
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
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
                    modifier = Modifier.weight(0.5f),
                    isEditing = isEditing,
                    imageColorUri = uiState.currentImageSelected,
                    onImageSelected = {
                        onImageSelected(it)
                    },
                    onDoneColorClick = {
                        onDoneImageClick()
                    }
                )
            }
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
                    text = "Phân loại",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }

//            val isEditingList = remember { mutableStateListOf<Boolean>() }
//
//            LaunchedEffect(uiState.listProductOptions) {
//                isEditingList.clear()
//                isEditingList.addAll(List(uiState.listProductOptions.size) { false })
//            }

            val isEditingList = remember {
                mutableStateListOf<Boolean>().apply {
                    repeat(uiState.listProductOptions.size) { add(false) }
                }
            }

// Khi danh sách thay đổi kích thước, cập nhật danh sách isEditingList mà không mất trạng thái
            LaunchedEffect(uiState.listProductOptions.size) {
                if (isEditingList.size < uiState.listProductOptions.size) {
                    repeat(uiState.listProductOptions.size - isEditingList.size) {
                        isEditingList.add(false)
                    }
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                uiState.listProductOptions.forEachIndexed { index, productOption ->
                    AddOptionTextField(
                        isEditing = isEditingList.getOrElse(index) { false },
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
                                it
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
                    text = "Thêm phân loại",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }

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
//            val isColorOptionEditingList =
//                remember { mutableStateListOf(*Array(uiState.listColorOptions.size) { false }) }
            val isColorOptionEditingList = remember {
                mutableStateListOf<Boolean>().apply {
                    repeat(uiState.listColorOptions.size) { add(false) }
                }
            }

// Khi danh sách thay đổi kích thước, cập nhật danh sách isEditingList mà không mất trạng thái
            LaunchedEffect(uiState.listColorOptions.size) {
                if (isColorOptionEditingList.size < uiState.listColorOptions.size) {
                    repeat(uiState.listColorOptions.size - isColorOptionEditingList.size) {
                        isColorOptionEditingList.add(false)
                    }
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                uiState.listColorOptions.forEachIndexed { index, colorOption ->
                    ColorImagePicker(
                        isEditing = isColorOptionEditingList.getOrElse(index) { false },
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
//                val isStockByVariantList =
//                    remember { mutableStateListOf(*Array(uiState.listStockByVariant.size) { false }) }
                val isStockByVariantList = remember {
                    mutableStateListOf<Boolean>().apply {
                        repeat(uiState.listStockByVariant.size) { add(false) }
                    }
                }

// Khi danh sách thay đổi kích thước, cập nhật danh sách isEditingList mà không mất trạng thái
                LaunchedEffect(uiState.listStockByVariant.size) {
                    if (isStockByVariantList.size < uiState.listStockByVariant.size) {
                        repeat(uiState.listStockByVariant.size - isStockByVariantList.size) {
                            isStockByVariantList.add(false)
                        }
                    }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    uiState.listStockByVariant.forEachIndexed { index, variant ->
                        AddStockByVariantField(
                            isEditing = isStockByVariantList.getOrElse(index) { false },
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
                            stockByVariant = uiState.listStockByVariant,
                            quantity = variant.quantity.toString(),
                            onQuantityChange = {
                                onEditQuantityByVariantChange(index, it)
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
//            HorizontalDivider(
//                thickness = 1.dp,
//                modifier = Modifier.padding(16.dp)
//            )

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
                    stockByVariant = uiState.listStockByVariant,
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
    listColorOptions: List<ColorOptionsForAddProduct> = emptyList(),
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
    val optionPriceToString = try {
        if (optionPrice == 0.0) {
            ""
        } else if (isEditing) {
            formatNumber(optionPrice)
        } else {
            formatCurrency2(optionPrice)
        }
    } catch (e: NumberFormatException) {
        "" // Nếu có lỗi khi chuyển đổi số
    }
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
        var selectedCategory by remember { mutableStateOf("0") }
//        val listCategory = DataDummy.categoryList.map { it.name }
        val category = CategoryModel(
            id = "0",
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
