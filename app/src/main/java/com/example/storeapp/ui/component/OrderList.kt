package com.example.storeapp.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.storeapp.R
import com.example.storeapp.ui.theme.StoreAppTheme

@Composable
fun OrderList(modifier: Modifier = Modifier) {
    val options = listOf("Tất cả", "Hoàn thành", "Đang giao", "Đã hủy")
    LazyColumn(
        modifier = modifier
            .height(800.dp),
//            .padding(start = 8.dp, end = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)

    ) {
        items(options) { item ->
            OrderItem(status = item)
        }
    }
}

@Composable
fun OrderItem(
    status: String,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()  // Chiếm hết chiều ngang còn lại
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.icon_order),
                        contentDescription = "orderIcon",
                        modifier = Modifier
                            .size(24.dp)
                            .aspectRatio(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "#HD0921",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                }
                Column(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    VerticalDivider(
                        thickness = 1.dp,
                        color = Color.Black,
                        modifier = Modifier
                            .height(24.dp)
                            .padding(start = 3.dp)
                    )
                    // Circle at the bottom
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(Color.Black, shape = CircleShape)
//                        .align(Alignment.BottomStart) // Đặt chấm tròn ở dưới cùng
                    )
                }
                DateOrder(
                    imageId = R.drawable.icon_calendar,
                    dateOrderText = "Ngày xuất kho:",
                    date = "02/04/2024",
                    modifier = modifier
                )
                DateOrder(
                    imageId = R.drawable.icon_calendar,
                    dateOrderText = "Ngày nhận(dự kiến):",
                    date = "02/04/2024"
                )
                DateOrder(
                    imageId = R.drawable.icon_pinlocation,
                    dateOrderText = "Tp.Hồ Chí Minh, Việt Nam",
//            date = "02/04/2024"
                )
            }
            OrderStatus(status = status)
        }

    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OrderItemPreview() {
    StoreAppTheme {
        val options = listOf("Tất cả", "Đang giao", "Hoàn thành", "Đã hủy")
        OrderItem(status = "Hoàn thành")
    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OrderListPreview() {
    StoreAppTheme {
        val options = listOf("Tất cả", "Đang giao", "Hoàn thành", "Đã hủy")
        OrderList(modifier = Modifier.padding(start = 16.dp, end = 16.dp))
    }
}



