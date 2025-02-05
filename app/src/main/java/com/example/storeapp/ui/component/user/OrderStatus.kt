package com.example.storeapp.ui.component.user

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.storeapp.R
import com.example.storeapp.ui.theme.StoreAppTheme

@Composable
fun OrderStatus(
    status: String,
//    modifier: Modifier = Modifier
) {
    // Xác định màu sắc dựa trên trạng thái
    val containerColor = when (status.lowercase()) {
        "đang giao" -> colorResource(id = R.color.blue) // Màu xanh dương
        "hoàn thành" -> Color(0xFF4CAF50) // Màu xanh lá
        "đã hủy" -> Color(0xFFF44336) // Màu đỏ
        else -> Color(0xFF9E9E9E) // Màu xám mặc định
    }
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = Color.White // Màu chữ
        ),
//        modifier = Modifier.height(36.dp)
    ) {
        Text(
            text = status,
            modifier = Modifier.padding(20.dp, 8.dp),
            fontWeight = FontWeight.Bold
        )
    }

}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OrderStatusPreview() {
    StoreAppTheme {
        OrderStatus(status = "Đang giao")
    }
}