package com.example.storeapp.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.storeapp.ui.theme.StoreAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterOrder(
    modifier: Modifier = Modifier,
    options: List<String>, // Danh sách trạng thái hoặc mục để chọn
    onOptionSelected: (String) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) } // Kiểm soát trạng thái menu
    var selectedOption by rememberSaveable { mutableStateOf("") } // Lựa chọn hiện tại

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
//                        .clickable { expanded = !expanded }
                        .padding(8.dp),
                    tint = MaterialTheme.colorScheme.outline
                )
            },
            placeholder = {
                Text(
                    text = "Chọn trạng thái",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFE3E3E3),
                unfocusedContainerColor = Color(0xFFE3E3E3),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            modifier = modifier
                .menuAnchor() // Gắn menu vào TextField
                .fillMaxWidth()
//                .height(50.dp)
                .border(
                width = 3.dp,
                color = Color("#D1D5DB".toColorInt()),
                shape = RoundedCornerShape(12.dp)
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedOption = option
                        expanded = false
                        onOptionSelected(option) // Gửi kết quả ra ngoài
                    }
                )
            }
        }
    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FilterOrderPreview() {
    StoreAppTheme {
        val options = listOf("Tất cả", "Đang giao", "Hoàn thành", "Đã hủy")
        FilterOrder(options = options)
    }
}

//Box {
//    Button(onClick = { iExpanded.value = true }) {
//        Text(inputUnit.value)
//        Icon(
//            Icons.Default.ArrowDropDown,
//            contentDescription = "Arrow Down"
//        )
//    }
//    DropdownMenu(
//        expanded = iExpanded.value,
//        onDismissRequest = { iExpanded.value = false }) {
//        DropdownMenuItem(
//            text = { Text("Centimeters") },
//            onClick = {
//                iExpanded.value = false
//                inputUnit.value = "Centimeters"
//                conversionFactor.value = 0.01
//                convertUnits()
//            })
//        DropdownMenuItem(
//            text = { Text("Meters") },
//            onClick = {
//                iExpanded.value = false
//                inputUnit.value = "Meters"
//                conversionFactor.value = 1.0
//                convertUnits()
//            })
//        DropdownMenuItem(
//            text = { Text("Feet") },
//            onClick = {
//                iExpanded.value = false
//                inputUnit.value = "Feet"
//                conversionFactor.value = 0.3048
//                convertUnits()
//            })
//        DropdownMenuItem(
//            text = { Text("Milimeters") },
//            onClick = {
//                iExpanded.value = false
//                inputUnit.value = "Milimeters"
//                conversionFactor.value = 0.001
//                convertUnits()
//            })
//    }
//}