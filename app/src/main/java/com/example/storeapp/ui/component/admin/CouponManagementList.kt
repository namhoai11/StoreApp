package com.example.storeapp.ui.component.admin

import android.content.res.Configuration
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.storeapp.data.local.DataDummy
import com.example.storeapp.model.CouponModel
import com.example.storeapp.model.CouponType
import com.example.storeapp.ui.component.function.formatCurrency2
import com.example.storeapp.ui.component.function.timestampToDateString
import com.example.storeapp.ui.theme.StoreAppTheme


@Composable
fun CouponManagementList(
    modifier: Modifier = Modifier,
    listCoupon: List<CouponModel>
) {
    LazyColumn(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(listCoupon) { item ->
            CouponManagementItem(couponItem = item)
        }
    }
}

@Composable
fun CouponManagementItem(
    couponItem: CouponModel,
) {
    val value = when (couponItem.type) {
        CouponType.PERCENTAGE -> "${couponItem.value * 100}%"
        CouponType.FIXED_AMOUNT -> formatCurrency2(couponItem.value)
        CouponType.FREE_SHIPPING -> "free ship"
    }
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
//            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = couponItem.code,
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
            )
            HorizontalDivider(
                thickness = 2.dp,
                modifier = Modifier.padding(start = 64.dp, end = 64.dp, bottom = 16.dp)
            )
            Column(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Bắt đầu:",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                    )
                    Text(
                        text = timestampToDateString(couponItem.startDate),
                        color = Color.Gray,
                        fontSize = 14.sp,
                    )

                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Kết thúc:",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                    )
                    Text(
                        text = timestampToDateString(couponItem.startDate),
                        color = Color.Gray,
                        fontSize = 14.sp,
                    )

                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Số lượng:",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                    )
                    Text(
                        text = "${couponItem.quantity}",
                        color = Color.Gray,
                        fontSize = 14.sp,
                    )

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Loại:",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                    )
                    Text(
                        text = couponItem.type.toString(),
                        color = Color.Gray,
                        fontSize = 14.sp,
                    )

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Giá trị:",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                    Text(
                        text = value,
                        color = Color.Gray,
                        fontSize = 18.sp,
                    )
                }
            }
        }
    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewListCouponManagement() {
    val listCoupon = DataDummy.dummyCoupon
    StoreAppTheme {
        CouponManagementList(listCoupon = listCoupon)
    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewCouponManagementItem() {
    val couponItem = DataDummy.coupon
    StoreAppTheme {
        CouponManagementItem(couponItem)
    }
}

//fun formatCurrency2(amount: Double): String {
//    val formatter = DecimalFormat("#,###")
//    return formatter.format(amount) + " ₫"
//}
//
//fun getTotalProductCount(order: OrderModel): Int {
//    return order.products.sumOf { it.quantity }
//}
