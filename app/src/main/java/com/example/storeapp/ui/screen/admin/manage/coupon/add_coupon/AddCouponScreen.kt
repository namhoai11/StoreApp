package com.example.storeapp.ui.screen.admin.manage.coupon.add_coupon

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.storeapp.R
import com.example.storeapp.data.local.DataDummy
import com.example.storeapp.model.CouponType
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.admin.AdminTopAppBar
import com.example.storeapp.ui.component.function.timestampToDateOnlyString
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.theme.StoreAppTheme
import java.util.Calendar

//object AddCouponManagementDestination : NavigationDestination {
//    override val route = "addcouponmanagement/?couponId={couponId}&isEditing={isEditing}"
//    override val titleRes = R.string.addcouponmanage_title
//
//    fun createRoute(couponId: String?, isEditing: Boolean): String {
//        return if (couponId == null) {
//            "addcouponmanagement/?isEditing=$isEditing"  // Giữ format nhất quán
//        } else {
//            "addcouponmanagement/?couponId=$couponId&isEditing=$isEditing"
//        }
//    }
//}

object AddCouponManagementDestination : NavigationDestination {
    override val route = "addcouponmanagement?couponId={couponId}&isEditing={isEditing}"
    override val titleRes = R.string.addcouponmanage_title

    fun createRoute(couponId: String?, isEditing: Boolean): String {
        return if (couponId == null) {
            "addcouponmanagement?isEditing=$isEditing"
        } else {
            "addcouponmanagement?couponId=$couponId&isEditing=$isEditing"
        }
    }
}



@Composable
fun AddCouponScreen(
    navController: NavController,
    viewModel: AddCouponViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by viewModel.uiState.collectAsState()
    val couponId = uiState.couponDetailsItem.id
    val isEditing = uiState.isEditing
    val textRole = when {
        couponId == "" && isEditing -> {
            "Thêm"
        }

        couponId != "" && isEditing -> {
            "Sửa"
        }

        else -> {
            "Chi tiết"
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadCoupon()
    }

    Scaffold(
        topBar = {
            AdminTopAppBar(
                R.drawable.arrowback,
                textRole,
                "Khuyến mãi",
                { navController.navigateUp() },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 48.dp)
            )
        },
    ) { innerPadding ->
        AddCouponContent(
            innerPadding = innerPadding,
            uiState = uiState,
            isEditing = uiState.isEditing,
            editCouponClick = { viewModel.editCouponClicked() },
            deleteCouponClick = { viewModel.removeCoupon { navController.navigateUp() } },
            onNameChange = { if (uiState.isEditing) viewModel.onNameChange(it) },
            onQuantityChange = { if (uiState.isEditing) viewModel.onQuantityChange(it) },
            onTypeSelected = { if (uiState.isEditing) viewModel.onTypeSelected(it) },
            onValueChange = { if (uiState.isEditing) viewModel.onValueChange(it) },
            onStartDateChange = { if (uiState.isEditing) viewModel.onStartDateChange(it) },
            onEndDateChange = { if (uiState.isEditing) viewModel.onEndDateChange(it) },
            onDescriptionChange = { if (uiState.isEditing) viewModel.onDescriptionChange(it) },
            onConfirm = {
                viewModel.addOrUpdateCoupon()
            }
        )
    }
}


@Composable
fun AddCouponContent(
    innerPadding: PaddingValues,
    uiState: AddCouponUiState,
    isEditing: Boolean,
    editCouponClick: () -> Unit = {},
    deleteCouponClick: () -> Unit = {},
    onNameChange: (String) -> Unit = {},
    onQuantityChange: (String) -> Unit = {},
    onTypeSelected: (CouponType) -> Unit = {},
    onValueChange: (String) -> Unit = {},
    onStartDateChange: (String) -> Unit = {},
    onEndDateChange: (String) -> Unit = {},
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
                    text = "Khuyến mãi",
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
                                editCouponClick()
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
                                deleteCouponClick()
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
                    text = "Code",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = uiState.couponDetailsItem.code,
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
                valueInput = uiState.couponDetailsItem.name,
                onValueChange = onNameChange,

                )
            AddTextField(
                title = "Số lượng",
                isEditing = isEditing,
                valueInput = uiState.quantityInput,
                onValueChange = onQuantityChange,
                isNumberInput = true
            )

            AddTypeField(
                title = "Loại",
                isEditing = isEditing,
                selectedType = uiState.couponDetailsItem.type,
                onTypeSelected = onTypeSelected
            )

            AddTextField(
                title = "Giá trị",
                isEditing = isEditing,
                valueInput = uiState.valueInput,
                onValueChange = onValueChange,
                isNumberInput = true,
            )

            AddDateField(
                title = "Bắt đầu",
                isEditing = isEditing,
                dateInput = timestampToDateOnlyString(uiState.couponDetailsItem.startDate),
                onDateChange = onStartDateChange
            )
            AddDateField(
                title = "Kết thúc",
                isEditing = isEditing,
                dateInput = timestampToDateOnlyString(uiState.couponDetailsItem.endDate),
                onDateChange = onEndDateChange
            )
            AddLargeTextField(
                title = "Mô tả",
                isEditing = isEditing,
                valueInput = uiState.couponDetailsItem.description,
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
fun AddTextField(
//    modifier: Modifier = Modifier,
    isEditing: Boolean,
    title: String,
    valueInput: String = "",
    onValueChange: (String) -> Unit,
    isNumberInput: Boolean = false // Mặc định là false (nhập text)
) {
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
        Box(
            modifier = Modifier
                .width(250.dp)
                .height(66.dp)
                .border(1.dp, Color(0xFF7D32A8), shape = RoundedCornerShape(12.dp)), // Viền ngoài
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize() // Đảm bảo kích thước bằng với Box cha
                    .background(
                        if (isEditing) Color.Transparent else Color.LightGray,
                        shape = RoundedCornerShape(12.dp)
                    ) // Nền bên trong viền
                    .padding(4.dp) // Padding chỉ áp dụng lên nội dung bên trong
            ) {
                TextField(
                    value = valueInput,
                    onValueChange = onValueChange,
                    enabled = isEditing,
                    modifier = Modifier.fillMaxSize(),
                    keyboardOptions = if (isNumberInput) {
                        KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    } else {
                        KeyboardOptions.Default
                    },
                    textStyle = TextStyle(
                        textAlign = TextAlign.Start,
                        color = Color(0xFF7D32A8),
                        fontSize = 16.sp
                    ),
                    visualTransformation = VisualTransformation.None,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    )
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
fun AddLargeTextField(
    modifier: Modifier = Modifier,
    title: String,
    isEditing: Boolean,
    valueInput: String = "",
    onValueChange: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Tiêu đề (Title)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .background(Color.Gray, shape = CircleShape)
            )
            Text(
                text = title,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(8.dp)
            )
        }

        // TextField nhập mô tả
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .border(1.dp, Color(0xFF7D32A8), shape = RoundedCornerShape(12.dp)), // Viền ngoài
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize() // Đảm bảo kích thước bằng với Box cha
                    .background(
                        if (isEditing) Color.Transparent else Color.LightGray,
                        shape = RoundedCornerShape(12.dp)
                    ) // Nền bên trong viền
                    .padding(4.dp) // Padding chỉ áp dụng lên nội dung bên trong
            ) {
                TextField(
                    value = valueInput,
                    onValueChange = onValueChange,
                    enabled = isEditing,
                    modifier = modifier
                        .fillMaxSize(),
                    textStyle = TextStyle(
                        textAlign = TextAlign.Start,
                        color = Color(0xFF7D32A8),
                        fontSize = 16.sp
                    ),
                    maxLines = Int.MAX_VALUE, // Cho phép nhập nhiều dòng
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    visualTransformation = VisualTransformation.None,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    )
                )
            }


        }
        HorizontalDivider(
            thickness = 1.dp,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun AddDateField(
    title: String,
    isEditing: Boolean,
    dateInput: String = "",
    onDateChange: (String) -> Unit

) {
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
        DatePickerField(
            isEditing = isEditing,
            selectedDate = dateInput,
            onDateSelected = onDateChange
        )
    }
    HorizontalDivider(
        thickness = 1.dp,
        modifier = Modifier.padding(16.dp)
    )
}

@SuppressLint("DefaultLocale")
@Composable
fun DatePickerField(
//    modifier: Modifier = Modifier,
    isEditing: Boolean,
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
            onDateSelected(formattedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    Box(
        modifier = Modifier
            .width(250.dp)
            .height(66.dp)
            .border(1.dp, Color(0xFF7D32A8), shape = RoundedCornerShape(12.dp)), // Viền ngoài
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .matchParentSize() // Đảm bảo kích thước bằng với Box cha
                .background(
                    if (isEditing) Color.Transparent else Color.LightGray,
                    shape = RoundedCornerShape(12.dp)
                ) // Nền bên trong viền
                .padding(4.dp) // Padding chỉ áp dụng lên nội dung bên trong
        ) {
            OutlinedTextField(
                value = selectedDate,
                onValueChange = {},
                readOnly = true,
                enabled = isEditing,
                modifier = Modifier
                    .fillMaxSize(),
                trailingIcon = {
                    Icon(
                        Icons.Default.DateRange, contentDescription = "Chọn ngày",
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { datePickerDialog.show() }
                    )
                },
                textStyle = TextStyle(
                    textAlign = TextAlign.Start,
                    color = Color(0xFF7D32A8),
                    fontSize = 16.sp
                ),
                visualTransformation = VisualTransformation.None,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,  // Thêm dòng này
                    errorIndicatorColor = Color.Transparent     // Nếu có trạng thái lỗi
                )
            )
        }

    }

}

@Composable
fun AddTypeField(
//    modifier: Modifier = Modifier,
    title: String,
    isEditing: Boolean,
    selectedType: CouponType,
    onTypeSelected: (CouponType) -> Unit
) {
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
        FilterType(
            isEditing = isEditing,
            selectedType = selectedType,
            onTypeSelected = onTypeSelected
        )
    }
    HorizontalDivider(
        thickness = 1.dp,
        modifier = Modifier.padding(16.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterType(
    modifier: Modifier = Modifier,
    isEditing: Boolean,
    selectedType: CouponType,
    onTypeSelected: (CouponType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) } // Kiểm soát trạng thái menu
    var selectedOption by remember { mutableStateOf(selectedType.toString()) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (isEditing) expanded = !expanded }
    ) {
        Box(
            modifier = Modifier
                .width(250.dp)
                .height(66.dp)
                .border(1.dp, Color(0xFF7D32A8), shape = RoundedCornerShape(12.dp)), // Viền ngoài
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize() // Đảm bảo kích thước bằng với Box cha
                    .background(
                        if (isEditing) Color.Transparent else Color.LightGray,
                        shape = RoundedCornerShape(12.dp)
                    ) // Nền bên trong viền
                    .padding(4.dp) // Padding chỉ áp dụng lên nội dung bên trong
            ) {
                TextField(
                    value = selectedOption,
                    onValueChange = { },
                    readOnly = true,
                    enabled = isEditing, // Chặn chỉnh sửa nếu không trong trạng thái sửa
                    trailingIcon = {
                        Icon(
                            Icons.Default.ArrowDropDown, // Thay bằng icon dropdown
                            contentDescription = "Dropdown Icon",
                            modifier = Modifier
                                .padding(8.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                    },
                    textStyle = TextStyle(
                        textAlign = TextAlign.Start,
                        color = Color(0xFF7D32A8),
                        fontSize = 16.sp
                    ),
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = VisualTransformation.None,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,  // Thêm dòng này
                        errorIndicatorColor = Color.Transparent     // Nếu có trạng thái lỗi
                    ),
                    modifier = modifier
                        .menuAnchor() // Gắn menu vào TextField
                        .fillMaxSize()
                )
            }

        }
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            CouponType.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.toString()) },
                    onClick = {
                        selectedOption = option.toString()
                        expanded = false
                        onTypeSelected(option) // Gửi kết quả ra ngoài
                    }
                )
            }
        }
    }
}


@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAddCouponContent() {
    StoreAppTheme {
        AddCouponContent(
            innerPadding = PaddingValues(0.dp),
            uiState = DataDummy.addCouponUiState,
            isEditing = false
        )
    }
}

@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAddTextField() {
    StoreAppTheme {
        AddTextField(
            title = "So Luong",
            isEditing = true,
            onValueChange = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDatePickerField() {
    var selectedDate by remember { mutableStateOf("01/01/2024") }

    DatePickerField(
        isEditing = true,
        selectedDate = selectedDate,
        onDateSelected = { newDate -> selectedDate = newDate }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewAddDateField() {
    var selectedDate by remember { mutableStateOf("01/01/2024") }

    AddDateField(
        title = "Ngày sinh",
        isEditing = true,
        dateInput = selectedDate,
        onDateChange = { newDate -> selectedDate = newDate }
    )
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FilterTypePreview() {
    StoreAppTheme {
        var selectedType by remember { mutableStateOf(CouponType.PERCENTAGE) }

        FilterType(
            isEditing = true,
            selectedType = selectedType,
            onTypeSelected = { selectedType = it }
        )
    }
}



