﻿package com.example.storeapp.ui.component.admin

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.storeapp.data.local.DataDummy
import com.example.storeapp.model.OrderModel
import com.example.storeapp.model.UserModel
import com.example.storeapp.ui.component.function.formatCurrency2
import com.example.storeapp.ui.component.function.timestampToDateString
import com.example.storeapp.ui.component.user.OrderStatus
import com.example.storeapp.ui.theme.StoreAppTheme

@Composable
fun OrderManagementList(
    listOrder: List<OrderModel>,
    userMap: Map<String, UserModel> = emptyMap(), // 🆕 Thêm userMap
    onOrderItemClick: (OrderModel) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(listOrder) { item ->
            val user = userMap[item.userId]
            OrderManagementItem(
                orderItem = item,
                user = user,
                orderItemClick = { onOrderItemClick(item) })
        }
    }
}

@Composable
fun OrderManagementItem(
    orderItem: OrderModel,
    user: UserModel? = null,
    orderItemClick: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = Modifier.clickable {
            orderItemClick()
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 8.dp)
                    .weight(1f),
            ) {
                Text(
                    text = orderItem.orderCode,
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = timestampToDateString(orderItem.createdAt),
                    color = Color.Gray,
//                            fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
            OrderStatus(modifier = Modifier.weight(0.5f), status = orderItem.status)
        }
        HorizontalDivider(
            thickness = 2.dp,
            modifier = Modifier.padding(start = 64.dp, end = 64.dp, bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
                    .weight(1f),
            ) {
                Text(
                    text = "Thông tin",
                    color = Color.Gray,
                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = formatCurrency2(orderItem.totalPrice),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
//                    modifier = Modifier.alignByBaseline() // Canh theo baseline
                )
                Text(text = "(${getTotalProductCount(orderItem)} san pham)")
            }
            Column(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.End // Căn lề phải cho toàn bộ nội dung
            ) {
                Text(
                    text = "Khách hàng",
                    color = Color.Gray,
                    fontSize = 18.sp
                )
                if (user != null) {
                    Text(
                        text = user.firstName + " " + user.lastName,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        text = "(${user.id})"
                    )
                } else {
                    Text(
                        text = "Khách hàng không tồn tại",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewListOrderManagement() {
    val listOrder = DataDummy.listOrder
    StoreAppTheme {
        OrderManagementList(listOrder = listOrder)
    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewOrderManagementItem() {
    val orderItem = DataDummy.order
    StoreAppTheme {
        OrderManagementItem(orderItem)
    }
}


fun getTotalProductCount(order: OrderModel): Int {
    return order.products.sumOf { it.quantity }
}