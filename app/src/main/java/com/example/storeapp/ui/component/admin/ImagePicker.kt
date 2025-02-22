package com.example.storeapp.ui.component.admin

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import com.example.storeapp.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun ImagePicker(
    modifier: Modifier=Modifier,
    isEditing: Boolean,
    imageColorUri: Uri?,
    onImageSelected: (Uri) -> Unit = {},
    onDoneColorClick: () -> Unit = {},
    onEditColorClick: () -> Unit = {},
    onDeleteColorClick: () -> Unit = {},
) {
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { onImageSelected(it) }
        }
    Row(
//        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Box(
            modifier = Modifier
                .size(75.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray)
                .then(if (isEditing) Modifier.clickable { launcher.launch("image/*") } else Modifier),
            contentAlignment = Alignment.Center
        ) {
            if (imageColorUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(imageColorUri),
                    contentDescription = "Ảnh màu",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.icon_camera),
                    contentDescription = "Thêm ảnh",
                    tint = Color.White,
                    modifier = Modifier.padding(12.dp)
                        .alpha(if (isEditing) 1f else 0.5f) // Mờ icon nếu không cho chỉnh sửa
                )
            }
        }
        if (!isEditing) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(Color.Cyan, R.drawable.edit, "Edit", onEditColorClick)
                IconButton(Color.Red, R.drawable.icon_trash, "Delete", onDeleteColorClick)
            }
        } else {
            IconButton(Color.Green, R.drawable.icon_check_mark, "Done", onDoneColorClick)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewImagePicker() {
    ImagePicker(
        modifier = Modifier.size(75.dp),
        isEditing = true,  // Chuyển thành true để hiển thị nút Done
        imageColorUri = null,
        onEditColorClick = { println("Edit Clicked") },
        onDeleteColorClick = { println("Delete Clicked") },
        onDoneColorClick = { println("Done Clicked") }
    )
}

