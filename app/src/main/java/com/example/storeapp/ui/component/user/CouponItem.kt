package com.example.storeapp.ui.component.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.storeapp.data.local.DataDummy
import com.example.storeapp.model.CouponModel
import com.example.storeapp.model.CouponType
import com.example.storeapp.ui.component.function.formatCurrency2
import com.example.storeapp.ui.theme.StoreAppTheme

@Composable
fun CouponItem(
    modifier: Modifier = Modifier,
    item:CouponModel,
    onChoose: () -> Unit,
    isChoose: Boolean,
) {
    val value = when (item.type) {
        CouponType.PERCENTAGE -> "${item.value * 100}%"
        CouponType.FIXED_AMOUNT -> formatCurrency2(item.value)
        CouponType.FREE_SHIPPING -> "free ship"
    }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onChoose() }
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = value,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = item.description,
                    fontSize = 12.sp,
                )

            }
            RadioButton(
                selected = isChoose,
                onClick = onChoose
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CouponItemPreview() {
    StoreAppTheme {
        CouponItem(
            item = DataDummy.coupon,
            onChoose = {},
            isChoose = false,
//            discount = "25%",
//            description = "Applies to get 25% off",
//            expiredDate = "31 Desember 2024",
//            color1 = Color(0xFFFFA726),
//            color2 = Color(0xFFFFD54F)
        )
    }
}