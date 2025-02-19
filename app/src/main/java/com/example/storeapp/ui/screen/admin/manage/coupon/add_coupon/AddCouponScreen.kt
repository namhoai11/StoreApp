package com.example.storeapp.ui.screen.admin.manage.coupon.add_coupon

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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

object AddCouponManagementDestination : NavigationDestination {
    override val route = "addcouponmanagement"
    override val titleRes = R.string.addcouponmanage_title
}

@Composable
fun AddCouponScreen(
    navController: NavController,
    viewModel: AddCouponViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            AdminTopAppBar(
                R.drawable.arrowback,
                "Điều chỉnh",
                "Khuyến mãi",
                { navController.navigateUp() },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 48.dp)
            )
        },
//        bottomBar = {
//            AdminBottomNavigationBar(
//                navController = navController,
//                currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
//
//            )
//        }
    ) { innerPadding ->
        AddCouponContent(
            innerPadding = innerPadding, uiState = uiState,
            onNameChange = { viewModel.onNameChange(it) },
            onQuantityChange = { viewModel.onQuantityChange(it) },
            onTypeSelected = { viewModel.onTypeSelected(it) },
            onValueChange = { viewModel.onValueChange(it) },
            onStartDateChange = { viewModel.onStartDateChange(it) },
            onEndDateChange = { viewModel.onEndDateChange(it) },
            onDescriptionChange = { viewModel.onDescriptionChange(it) },
            onConfirm = {
                viewModel.addCoupon { navController.navigateUp() }
            }
        )
    }
}


@Composable
fun AddCouponContent(
    innerPadding: PaddingValues,
    uiState: AddCouponUiState,
    onNameChange: (String) -> Unit = {},
    onQuantityChange: (String) -> Unit = {},
    onTypeSelected: (CouponType) -> Unit = {},
    onValueChange: (String) -> Unit = {},
    onStartDateChange: (String) -> Unit = {},
    onEndDateChange: (String) -> Unit = {},
    onDescriptionChange: (String) -> Unit = {},
    onConfirm: () -> Unit = {} // Hàm xử lý khi bấm "Xác nhận"
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        Column(
            modifier = Modifier
                .weight(1f) // Dùng weight để phần nội dung có thể cuộn được mà không che button
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Khuyến mãi",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
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
                valueInput = uiState.couponDetailsItem.name,
                onValueChange = onNameChange
            )
            AddTextField(
                title = "Số lượng",
                valueInput = uiState.quantityInput,
                onValueChange = onQuantityChange,
                isNumberInput = true
            )

            AddTypeField(
                title = "Loại",
                selectedType = uiState.couponDetailsItem.type,
                onTypeSelected = onTypeSelected
            )

            AddTextField(
                title = "Giá trị",
                valueInput = uiState.valueInput,
                onValueChange = onValueChange,
                isNumberInput = true,
            )

            AddDateField(
                title = "Bắt đầu",
                dateInput = timestampToDateOnlyString(uiState.couponDetailsItem.startDate),
                onDateChange = onStartDateChange
            )
            AddDateField(
                title = "Kết thúc",
                dateInput = timestampToDateOnlyString(uiState.couponDetailsItem.endDate),
                onDateChange = onEndDateChange
            )
            AddLargeTextField(
                title = "Mô tả",
                valueInput = uiState.couponDetailsItem.description,
                onValueChange = onDescriptionChange
            )
        }
        // Nút xác nhận cố định dưới cùng
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
                Text(
                    text = "Xác nhận",
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun AddTextField(
    modifier: Modifier = Modifier,
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
        TextField(
            value = valueInput,
            onValueChange = onValueChange,
            modifier = modifier
                .width(250.dp)
                .height(66.dp)
                .border(
                    1.dp, Color(0xFF7D32A8),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(4.dp),
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
                unfocusedIndicatorColor = Color.Transparent
            )
        )
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
        TextField(
            value = valueInput,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth()
                .height(150.dp) // Cao hơn để nhập mô tả dài
                .border(
                    1.dp, Color(0xFF7D32A8),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(4.dp),
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
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        // Thanh phân cách
        HorizontalDivider(
            thickness = 1.dp,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun AddDateField(
    title: String,
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
        DatePickerField(selectedDate = dateInput, onDateSelected = onDateChange)
    }
    HorizontalDivider(
        thickness = 1.dp,
        modifier = Modifier.padding(16.dp)
    )
}

@SuppressLint("DefaultLocale")
@Composable
fun DatePickerField(
    modifier: Modifier = Modifier,
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
        modifier = modifier
            .width(250.dp)
            .height(66.dp)
            .border(1.dp, Color(0xFF7D32A8), shape = RoundedCornerShape(12.dp))
            .clickable { datePickerDialog.show() }
            .padding(4.dp)
    ) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxSize(),
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
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }

}

@Composable
fun AddTypeField(
    modifier: Modifier = Modifier,
    title: String,
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
        FilterType(selectedType = selectedType, onTypeSelected = onTypeSelected)
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
    selectedType: CouponType,
    onTypeSelected: (CouponType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) } // Kiểm soát trạng thái menu
    var selectedOption by remember { mutableStateOf(selectedType.toString()) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedOption,
            onValueChange = { },
            readOnly = true,
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
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = modifier
                .menuAnchor() // Gắn menu vào TextField
                .width(250.dp)
                .height(66.dp)
                .border(
                    1.dp, Color(0xFF7D32A8),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(4.dp),
        )

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
            uiState = DataDummy.addCouponUiState
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
            onValueChange = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDatePickerField() {
    var selectedDate by remember { mutableStateOf("01/01/2024") }

    DatePickerField(
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
            selectedType = selectedType,
            onTypeSelected = { selectedType = it }
        )
    }
}



