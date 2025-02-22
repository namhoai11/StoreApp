package com.example.storeapp.ui.component.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.storeapp.ui.theme.StoreAppTheme

@Composable
fun AddProductTextField(
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
        AddValueTextField(
            modifier = Modifier.width(250.dp),
            isEditing = isEditing,
            valueInput = valueInput,
            onValueChange = onValueChange,
            isNumberInput = isNumberInput
        )

    }
    HorizontalDivider(
        thickness = 1.dp,
        modifier = Modifier.padding(16.dp)
    )
}

@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAddProductTextField() {
    StoreAppTheme {
        AddProductTextField(
            title = "So Luong",
            isEditing = true,
            onValueChange = {},
        )
    }
}