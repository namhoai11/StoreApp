package com.example.storeapp.ui.screen.admin.manage.category.add_category

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.admin.AdminTopAppBar
import com.example.storeapp.ui.component.admin.ImagePicker
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.screen.admin.manage.coupon.add_coupon.AddLargeTextField
import com.example.storeapp.ui.screen.admin.manage.coupon.add_coupon.AddTextField
import com.example.storeapp.ui.theme.StoreAppTheme


object AddCategoryManagementDestination : NavigationDestination {
    override val route = "addcategorymanagement?categoryId={categoryId}&isEditing={isEditing}"
    override val titleRes = R.string.addcouponmanage_title

    fun createRoute(categoryId: String?, isEditing: Boolean): String {
        return if (categoryId == null) {
            "addcategorymanagement?isEditing=$isEditing"
        } else {
            "addcategorymanagement?categoryId=$categoryId&isEditing=$isEditing"
        }
    }
}

@Composable
fun AddCategoryScreen(
    navController: NavController,
    viewModel: AddCategoryViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by viewModel.uiState.collectAsState()
    val categoryId = uiState.categoryDetailsItem.id
    val isEditing = uiState.isEditing
    val textRole = when {
        categoryId == "" && isEditing -> {
            "Thêm"
        }

        categoryId != "" && isEditing -> {
            "Sửa"
        }

        else -> {
            "Chi tiết"
        }
    }
    LaunchedEffect(Unit) {
        viewModel.loadCategory()
    }
    Scaffold(
        topBar = {
            AdminTopAppBar(
                R.drawable.arrowback,
                textRole,
                "Danh mục",
                { navController.navigateUp() },
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 16.dp)
            )
        },
    ) { innerPadding ->
        AddCategoryContent(
            innerPadding = innerPadding,
            uiState = uiState,
            isEditing = isEditing,
            editCategoryClick = { viewModel.editCategoryClicked() },
            deleteCategoryClick = { viewModel.removeCategory { navController.navigateUp() } },
            onNameChange = { viewModel.onNameChange(it) },
            onDescriptionChange = { viewModel.onDescriptionChange(it) },
            onImageSelected = { viewModel.onImageSelected(it) },
            onConfirm = { viewModel.saveCategory() }
        )
    }
}

@Composable
fun AddCategoryContent(
    innerPadding: PaddingValues,
    uiState: AddCategoryUiState,
    isEditing: Boolean,
    editCategoryClick: () -> Unit = {},
    deleteCategoryClick: () -> Unit = {},
    onNameChange: (String) -> Unit = {},
    onImageSelected: (Uri) -> Unit = {},
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
                    text = "Danh mục",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.weight(1f))
                if (!isEditing) {
                    Box(
                        contentAlignment = Alignment.Center, // Căn giữa nội dung trong Box
                        modifier = Modifier
                            .padding(10.dp)
                            .size(32.dp)
                            .background(
                                color = Color.Cyan,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable {
                                editCategoryClick()
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
                            .padding(10.dp)
                            .size(32.dp)
                            .background(
                                color = Color.Red,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable {
                                deleteCategoryClick()
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
                    text = uiState.categoryDetailsItem.id,
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
                valueInput = uiState.categoryDetailsItem.name,
                onValueChange = onNameChange,

                )
            // Biến trạng thái riêng cho ảnh duy nhất
            val isEditingImage = rememberSaveable { mutableStateOf(isEditing) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .background(Color.Gray, shape = CircleShape)
                    )
                    Text(
                        text = "Thêm ảnh",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                // Mục chọn ảnh duy nhất với trạng thái riêng biệt
                ImagePicker(
                    modifier = Modifier.weight(0.5f),
                    isEditing = isEditingImage.value,
                    imageColorUri = uiState.currentImageSelected,
                    onImageSelected = { onImageSelected(it) },
                    onEditColorClick = { isEditingImage.value = true },
                    onDoneColorClick = { isEditingImage.value = false }
                )
            }


            AddLargeTextField(
                title = "Mô tả",
                isEditing = isEditing,
                valueInput = uiState.categoryDetailsItem.description,
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

@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAddCouponContent() {
    StoreAppTheme {
        AddCategoryContent(
            innerPadding = PaddingValues(0.dp),
            uiState = DataDummy.addCategoryUiStateUiState,
            isEditing = false
        )
    }
}