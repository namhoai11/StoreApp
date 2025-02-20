package com.example.storeapp.ui.screen.address.add_address

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.storeapp.R
import com.example.storeapp.data.local.DataDummy
import com.example.storeapp.model.District
import com.example.storeapp.model.Province
import com.example.storeapp.model.Ward
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.theme.StoreAppTheme


object AddAddressDestination : NavigationDestination {
    override val route = "addaddress"
    override val titleRes = R.string.add_address_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAddressScreen(
    navController: NavController,
    viewModel: AddAddressViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Address",
//                        fontFamily = poppinsFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back",
                        modifier = Modifier
                            .clickable { navController.navigateUp() }
                            .padding(horizontal = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        AddAddressContent(
            innerPadding = innerPadding,
            uiState = uiState,
            onProvinceSelected = { viewModel.onProvinceSelected(it) },
            onDistrictSelected = { viewModel.onDistrictSelected(it) },
            onWardSelected = { viewModel.onWardSelected(it) },
            onStreetInput = { viewModel.onStreetInput(it) },
            onConfirm = {
                viewModel.onConfirm()
                navController.navigateUp()
            }
        )
    }

}


@Composable
fun AddAddressContent(
    innerPadding: PaddingValues,
    uiState: AddAddressUiState,
    onProvinceSelected: (Province) -> Unit,
    onDistrictSelected: (District) -> Unit,
    onWardSelected: (Ward) -> Unit,
    onStreetInput: (String) -> Unit,
    onConfirm: () -> Unit
) {
    Column(modifier = Modifier.padding(innerPadding)) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Chọn Tỉnh
            FilterLocation2(
                label = "Tỉnh/Thành phố",
                options = uiState.provinces.map { it.name },
//            selectedOption = uiState.selectedProvince?.name,
                onOptionSelected = { name ->
                    val province = uiState.provinces.find { it.name == name }
                    province?.let { onProvinceSelected(it) }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Chọn Huyện
            FilterLocation2(
                label = "Quận/Huyện",
                options = uiState.districts.map { it.name },
//            selectedOption = uiState.selectedDistrict?.name,
                onOptionSelected = { name ->
                    val district = uiState.districts.find { it.name == name }
                    district?.let { onDistrictSelected(it) }
                },
                enabled = uiState.selectedProvince != null
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Chọn Xã
            FilterLocation2(
                label = "Phường/Xã",
                options = uiState.wards.map { it.name },
//            selectedOption = uiState.selectedWard?.name,
                onOptionSelected = { name ->
                    val ward = uiState.wards.find { it.name == name }
                    ward?.let { onWardSelected(it) }
                },
                enabled = uiState.selectedDistrict != null
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Nhập tên đường
            InputStreet(label = "Nhập đường", onInput = onStreetInput)

            Spacer(modifier = Modifier.height(16.dp))

            // Nút Xác nhận
            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.selectedProvince != null && uiState.selectedDistrict != null && uiState.selectedWard != null && uiState.street.isNotEmpty()
            ) {
                Text("Xác nhận")
            }
        }

    }
}


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun FilterLocation(
//    label: String,
//    options: List<String>,
//    selectedOption: String?,
//    onOptionSelected: (String) -> Unit,
//    enabled: Boolean = true
//) {
//    var expanded by remember { mutableStateOf(false) }
//
//    Column {
//        Text(text = label, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
//
//        ExposedDropdownMenuBox(
//            expanded = expanded,
//            onExpandedChange = { if (enabled) expanded = !expanded }
//        ) {
//            TextField(
//                value = selectedOption ?: "Chọn $label",
//                onValueChange = {},
//                readOnly = true,
//                trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
//                modifier = Modifier
//                    .menuAnchor()
//                    .fillMaxWidth()
//                    .border(
//                        width = 1.dp,
//                        color = if (enabled) Color.Gray else Color.LightGray,
//                        shape = RoundedCornerShape(8.dp)
//                    ),
//                enabled = enabled,
//                colors = TextFieldDefaults.colors(
//                    unfocusedContainerColor = Color.White,
//                    focusedContainerColor = Color.White,
//                    disabledContainerColor = Color(0xFFF0F0F0),
//                    focusedIndicatorColor = Color.Transparent,
//                    unfocusedIndicatorColor = Color.Transparent
//                )
//            )
//
//            ExposedDropdownMenu(
//                expanded = expanded,
//                onDismissRequest = { expanded = false }
//            ) {
//                options.forEach { option ->
//                    DropdownMenuItem(
//                        text = { Text(option) },
//                        onClick = {
//                            onOptionSelected(option)
//                            expanded = false
//                        }
//                    )
//                }
//            }
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterLocation2(
    label: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    // Lọc danh sách theo chữ đã nhập
    val filteredOptions = options.filter { it.contains(searchText, ignoreCase = true) }

    Column {
        Text(text = label, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { if (enabled) expanded = !expanded }
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                readOnly = false, // Cho phép nhập chữ để tìm kiếm
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = if (enabled) Color.Gray else Color.LightGray,
                        shape = RoundedCornerShape(8.dp)
                    ),
                enabled = enabled,
                placeholder = { Text(text = "Nhập $label") },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    disabledContainerColor = Color(0xFFF0F0F0),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            ExposedDropdownMenu(
                expanded = expanded && filteredOptions.isNotEmpty(),
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
            ) {
                filteredOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            searchText = option // Gán chữ đã chọn vào TextField
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun InputStreet(
    modifier: Modifier = Modifier,
    onInput: (String) -> Unit = {},
    label: String,
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    Column {
        Text(text = label, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { text ->
                searchQuery = text
                onInput(text)
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.icon_pinlocation),
                    contentDescription = "Street",
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .scale(1f)
                        .height(50.dp),
                    tint = MaterialTheme.colorScheme.outline,
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                disabledContainerColor = Color(0xFFF0F0F0),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            placeholder = {
                Text(
                    text = "Nhập tên đường",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .offset(y = (-1.4).dp)
                )
            },
            modifier = modifier
                .border(
                    width = 3.dp,
                    color = Color("#D1D5DB".toColorInt()),
                    shape = RoundedCornerShape(12.dp)
                )
                .fillMaxWidth()
//            .height(50.dp)
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun SearchPreview() {
    StoreAppTheme(dynamicColor = false) {
        InputStreet(
            label = "Nhập đường",
            modifier = Modifier
                .padding(16.dp)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun AddAddressContentPreview() {
    val uiState = DataDummy.addAddressUiState
    AddAddressContent(
        innerPadding = PaddingValues(0.dp),
        uiState = uiState,
        onProvinceSelected = {},
        onDistrictSelected = {},
        onWardSelected = {},
        onStreetInput = {},
        onConfirm = {}
    )
}

//@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
//@Composable
//fun FilterLocationPreview() {
//    StoreAppTheme {
//        Column(modifier = Modifier.padding(16.dp)) {
//            val sampleProvinces = listOf("Hà Nội", "Hồ Chí Minh", "Đà Nẵng", "Hải Phòng")
//            var selectedProvince by remember { mutableStateOf<String?>(null) }
//
//            FilterLocation(
//                label = "Tỉnh/Thành phố",
//                options = sampleProvinces,
//                selectedOption = selectedProvince,
//                onOptionSelected = { selectedProvince = it }
//            )
//        }
//    }
//}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun FilterLocation2Preview() {
    StoreAppTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            val sampleProvinces = listOf("Hà Nội", "Hồ Chí Minh", "Đà Nẵng", "Hải Phòng")
            var selectedProvince by remember { mutableStateOf<String?>(null) }

            FilterLocation2(
                label = "Tỉnh/Thành phố",
                options = sampleProvinces,
                onOptionSelected = { selectedProvince = it }
            )
        }
    }
}

