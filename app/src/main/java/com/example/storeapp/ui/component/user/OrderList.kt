package com.example.storeapp.ui.component.user

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.storeapp.data.local.DataDummy
import com.example.storeapp.model.OrderModel
import com.example.storeapp.ui.component.function.timestampToDateOnlyString
import com.example.storeapp.ui.theme.StoreAppTheme

@Composable
fun OrderList(
    modifier: Modifier = Modifier,
    orderList: List<OrderModel>,
    orderItemClick: (OrderModel) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier
            .height(800.dp),
//            .padding(start = 8.dp, end = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)

    ) {
        items(orderList) { item ->
            OrderItem(item, orderItemClick = { orderItemClick(item) })
        }
    }
}

@Composable
fun OrderItem(
    orderItem: OrderModel,
    orderItemClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                orderItemClick()
            }
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
                        text = orderItem.orderCode,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
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
                    dateOrderText = "Ngày đặt:",
                    date = timestampToDateOnlyString(orderItem.updatedAt),
                    modifier = modifier
                )
                DateOrder(
                    imageId = R.drawable.icon_calendar,
                    dateOrderText = "Ngày nhận(dự kiến):",
                    date = timestampToDateOnlyString(orderItem.estimatedDeliveryDate)
                )
                DateOrder(
                    imageId = R.drawable.icon_pinlocation,
                    dateOrderText = orderItem.address.street + " - " + orderItem.address.ward + " - " + orderItem.address.district + " - " + orderItem.address.province,
                )
            }
            OrderStatus(status = orderItem.status, modifier = Modifier.weight(0.5f))
        }

    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OrderItemPreview() {
    StoreAppTheme {
        OrderItem(orderItem = DataDummy.order)
    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OrderListPreview() {
    StoreAppTheme {
        OrderList(
            orderList = DataDummy.listOrder,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )
    }
}



