package com.example.storeapp.ui.component.admin

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.storeapp.R


@Composable
fun ColorImagePicker(
    isEditing: Boolean,
    title: String,
    colorName: String = "",
    onColorNameChange: (String) -> Unit,
    imageColorUri: Uri?,
    onImageSelected: (Uri) -> Unit = {},
    onDoneColorClick: () -> Unit = {},
    onEditColorClick: () -> Unit = {},
    onDeleteColorClick: () -> Unit = {},
    isNumberInput: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AddValueTextField(
            modifier = Modifier.weight(1f),
            title = title,
            isEditing = isEditing,
            valueInput = colorName,
            onValueChange = onColorNameChange,
            isNumberInput = isNumberInput
        )

        ImagePicker(
            modifier = Modifier.weight(0.5f),
            isEditing = isEditing,
            imageColorUri = imageColorUri, onImageSelected = onImageSelected,
            onDoneColorClick = onDoneColorClick,
            onEditColorClick = onEditColorClick,
            onDeleteColorClick = onDeleteColorClick,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewColorImagePicker() {
//    val sampleColorOption = ColorOptions(
//        colorName = "Đỏ",
//        imageColorUri = null // Bạn có thể thay bằng một URI giả lập nếu muốn xem ảnh
//    )

    ColorImagePicker(
        title = "Loai",
        colorName = "Xanh",
        isEditing = false,
        onColorNameChange = {},
        isNumberInput = false,
        imageColorUri = null,
    )
}

