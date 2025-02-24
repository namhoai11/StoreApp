package com.example.storeapp.ui.component.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.storeapp.data.local.DataDummy
import com.example.storeapp.model.CategoryModel
import com.example.storeapp.ui.theme.StoreAppTheme
import com.google.firebase.Timestamp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterOption(
    modifier: Modifier = Modifier,
    isEditing: Boolean,
    listOptions: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) } // Kiểm soát trạng thái menu
    var selected by remember { mutableStateOf(selectedOption) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { if (isEditing) expanded = !expanded }
    ) {
        Box(
            modifier = modifier
//                .width(250.dp)
                .height(66.dp)
                .border(1.dp, Color(0xFF7D32A8), shape = RoundedCornerShape(12.dp)), // Viền ngoài
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = modifier
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
                    modifier = Modifier
                        .menuAnchor() // Gắn menu vào TextField
                        .fillMaxSize()
                )
            }

        }
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selected = option
                        expanded = false
                        onOptionSelected(option) // Gửi kết quả ra ngoài
                    }
                )
            }
        }
    }
}

@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FilterOptionPreview() {
    StoreAppTheme {
        var selectedCategory by remember { mutableStateOf("selectedCategory.name") }
        val listCategory = DataDummy.categoryList.map { it.name }
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
        FilterOption(
            modifier = Modifier.width(250.dp),
            isEditing = true,
            listOptions = listCategory,
            selectedOption = category.name,
            onOptionSelected = { selectedCategory = it }
        )
    }
}