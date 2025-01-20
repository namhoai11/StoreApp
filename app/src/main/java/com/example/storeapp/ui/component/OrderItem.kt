package com.example.storeapp.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.storeapp.R

@Preview
@Composable
fun OrderItem(
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
            OrderStatus(status = "Đang giao")
        }

    }
}

