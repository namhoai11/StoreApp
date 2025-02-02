package com.example.storeapp.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.storeapp.data.local.DataDummy
import com.example.storeapp.model.CouponModel
import com.example.storeapp.model.OrderModel
import com.example.storeapp.model.ShippingModel
import com.example.storeapp.model.UserLocationModel
import com.example.storeapp.ui.theme.StoreAppTheme
import com.google.firebase.Timestamp

@Composable
fun CheckoutContent(
    modifier: Modifier = Modifier,
    onEditAddress: () -> Unit,
    state: OrderModel? = null,
    selectedLocationId: Int?,
    selectedLocation: UserLocationModel,
    onShowDialog: () -> Unit,
    onChooseShipping: () -> Unit,
    onChooseCoupon: () -> Unit,
    selectedShippingId: Int?,
    shippingItem: List<ShippingModel>,
    selectedCouponId: Int?,
    couponItem: List<CouponModel>,
    isButtonEnabled: Boolean = false,
    onChoosePayment: () -> Unit,
    finalPrice: Double,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp
                )
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(
                    color = MaterialTheme.colorScheme.background
                )
                .weight(1f)
        ) {
            if (selectedLocationId == -1) {
                Card(
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color.LightGray
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth()
                        .clickable { onEditAddress() }
                        .wrapContentHeight()
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.Center),
                            text = "Choose Address",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            } else {
                Card(
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color.LightGray
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                        .clickable { onEditAddress() }
                        .height(125.dp)
                ) {
                    AddressItemScreen(
                        name = selectedLocation.street,
                        address = selectedLocation.province
                    )
                }
            }
            state?.products?.firstOrNull()?.let { item ->
                CartItemMini(
                    productName = item.product.name,
                    imageId = item.product.images.firstOrNull().toString(),
                    price = item.product.price,
                    orderCount = item.quantity,
                    totalOrder = item.quantity,
                    onDetailOrder = { onShowDialog() }
                )
            }
            if (selectedShippingId == null) {
                Card(
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color.LightGray
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable {
                            onChooseShipping()
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Choose Shipping",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Edit",
                                fontSize = 14.sp,
                            )
                            Spacer(modifier = Modifier.size(4.dp))
//                            Icon(
//                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
//                                contentDescription = "",
//                                modifier = Modifier
//                                    .size(20.dp)
//                            )
                        }
                    }
                }
            } else {
                val selectedShipping = shippingItem[selectedShippingId]
                Card(
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color.LightGray
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable {
                            onChooseShipping()
                        }
                ) {
                    ShippingItem(
                        isChoose = true,
                        onChoose = { onChooseShipping() },
                        name = selectedShipping.name,
                        price = selectedShipping.price,
                        description = selectedShipping.description
                    )
                }
            }
            if (selectedCouponId == null) {
                CouponInactiveSelected(
                    modifier = Modifier
                        .clickable { onChooseCoupon() }
                )
            } else {
                val selectedCouponChoose = couponItem[selectedCouponId]
                CouponItemSelected(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clickable { onChooseCoupon() },
                    discountTittle = selectedCouponChoose.maxDiscount.toString()
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier.padding(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                )
            ) {
                Text(
                    text = "Payment Summary",
                    fontWeight = FontWeight.SemiBold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Sub-total",
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = "$${"%.2f".format(state?.totalPrice)}",
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Shipping Charge",
                        color = MaterialTheme.colorScheme.outline
                    )
                    selectedShippingId?.let {
                        Text(
                            text = "$${shippingItem[selectedShippingId].price}",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } ?: run {
                        Text(
                            text = "$0.00",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Final Price",
                        color = MaterialTheme.colorScheme.outline
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (selectedCouponId != null) {
                            Text(
                                text = if (selectedShippingId == null) "$${"%.2f".format(state?.totalPrice)}"
                                else "$${
                                    "%.2f".format(
                                        (state?.totalPrice)?.plus(shippingItem[selectedShippingId].price)
                                    )
                                }",
                                textDecoration = TextDecoration.LineThrough,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "$${"%.2f".format(finalPrice)}",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.Black,
                )
                Button(
                    enabled = isButtonEnabled,
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                        .height(55.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    onClick = onChoosePayment,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Choose Payment",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CheckoutContentPreview() {
    StoreAppTheme(
        dynamicColor = false
    ) {
        CheckoutContent(
            onEditAddress = {},
            onShowDialog = {},
            onChooseShipping = {},
            selectedLocation = UserLocationModel(
                id = "1",
                street = "Jl. Durian No. 123",
                province = "Jawa Tengah",
                district = "Kab. Semarang",
                ward = "Banyubiru",
                isDefault = true,
                userId = "user123",
                provinceId = "province01",
                districtId = "district01",
                wardId = "ward01",
                latitude = -7.123456,
                longitude = 110.123456,
                createdAt = Timestamp.now(),
                updatedAt = Timestamp.now()
            ),
            selectedLocationId = -1,
            selectedShippingId = 2,
            shippingItem = DataDummy.dummyShipping,
            onChooseCoupon = {},
            selectedCouponId = 2,
            couponItem = DataDummy.dummyCoupon,
            onChoosePayment = {},
            finalPrice = 0.0
        )
    }
}