package com.example.storeapp.ui.component.user

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.storeapp.R
import com.example.storeapp.model.OrderStatus
import com.example.storeapp.ui.theme.StoreAppTheme

@Composable
fun OrderStatus(
    status: OrderStatus,
    modifier: Modifier = Modifier
) {
    // Xác định màu sắc dựa trên trạng thái
    val containerColor = when (status) {
        OrderStatus.AWAITING_PAYMENT -> Color(0xFFFF9800) // Cam (Chờ thanh toán)
        OrderStatus.PENDING -> Color(0xFFFFC107) // Vàng (Chờ xác nhận)
        OrderStatus.CONFIRMED -> Color(0xFF2196F3) // Xanh dương (Đã xác nhận)
        OrderStatus.SHIPPED -> Color(0xFF3F51B5) // Xanh dương đậm (Đang giao)
        OrderStatus.COMPLETED -> Color(0xFF4CAF50) // Xanh lá (Hoàn thành)
        OrderStatus.CANCELED -> Color(0xFFF44336) // Đỏ (Đã hủy)
        else -> Color(0xFF9E9E9E) // Xám (Mặc định)
    }
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = Color.White // Màu chữ
        ),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp, 8.dp),
            contentAlignment = Alignment.Center // 🏆 Căn giữa nội dung trong Box
        ) {
            Text(
                text = status.toString(),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                maxLines = 2
            )
        }
    }

}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OrderStatusPreview() {
    StoreAppTheme {
        OrderStatus(status = OrderStatus.AWAITING_PAYMENT)
    }
}