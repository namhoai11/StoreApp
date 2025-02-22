package com.example.storeapp.ui.component.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.storeapp.ui.theme.StoreAppTheme

@Composable
fun AddValueTextField(
    modifier: Modifier = Modifier,
    title:String="",
    isEditing: Boolean,
    valueInput: String,
    onValueChange: (String) -> Unit,
    isNumberInput: Boolean = false // Mặc định là false (nhập text)
) {
    Box(
        modifier = modifier
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
                onValueChange = {onValueChange(it)},
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
                label = { Text(text = title) },
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

@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAddValueTextField() {
    StoreAppTheme {
        AddValueTextField(
            modifier = Modifier.width(250.dp),
            title = "Pc",
            isEditing = true,
            valueInput = "",
            onValueChange = {},
        )
    }
}