package com.example.storeapp.ui.screen.checkout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.storeapp.R
import com.example.storeapp.data.local.DataDummy
import com.example.storeapp.model.CouponType
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.function.formatCurrency2
import com.example.storeapp.ui.component.user.AddressItemScreen
import com.example.storeapp.ui.component.user.CartMiniList
import com.example.storeapp.ui.component.user.CouponInactiveSelected
import com.example.storeapp.ui.component.user.CouponItemSelected
import com.example.storeapp.ui.component.user.ShippingItem
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.theme.StoreAppTheme


object CheckoutDestination : NavigationDestination {
    override val route = "checkout?locationId={locationId}"
    override val titleRes = R.string.addcouponmanage_title
    fun createRoute(locationId: String?): String {
        return "checkout?locationId=$locationId"
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    viewModel: CheckoutViewModel = viewModel(factory = AppViewModelProvider.Factory)

) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Thanh toán",
//                        fontFamily = poppinsFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back",
                        modifier = Modifier
                            .clickable { navController.navigateUp() }
                            .padding(horizontal = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        CheckoutContent(
            innerPadding = innerPadding,
            uiState = uiState,
            onEditAddress = { /*TODO*/ },
            onChooseShipping = { /*TODO*/ },
            onChooseCoupon = { /*TODO*/ }) {
        }
    }
}

@Composable
fun CheckoutContent(
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    uiState: CheckoutUiState,
    onEditAddress: () -> Unit,
    onChooseShipping: () -> Unit,
    onChooseCoupon: () -> Unit,
    onChoosePayment: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(innerPadding)
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
            if (uiState.selectedLocation == null) {
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
                            text = "Chọn địa chỉ",
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
                        name = uiState.selectedLocation.userName,
                        address = uiState.selectedLocation.street + " - " + uiState.selectedLocation.ward + " - " + uiState.selectedLocation.district + " - " + uiState.selectedLocation.province

                    )
                }
            }
            CartMiniList(products = uiState.products)
            if (uiState.selectedShipping == null) {
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
                            text = "Chọn phương thức vận chuyển",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Sửa",
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
                        name = uiState.selectedShipping.name,
                        price = uiState.selectedShipping.price,
                        description = uiState.selectedShipping.description
                    )
                }
            }
            if (uiState.selectedCoupon == null) {
                CouponInactiveSelected(
                    modifier = Modifier
                        .clickable { onChooseCoupon() }
                )
            } else {
                val value = when (uiState.selectedCoupon.type) {
                    CouponType.PERCENTAGE -> "${uiState.selectedCoupon.value * 100}%"
                    CouponType.FIXED_AMOUNT -> formatCurrency2(uiState.selectedCoupon.value)
                    CouponType.FREE_SHIPPING -> "free ship"
                }
                CouponItemSelected(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clickable { onChooseCoupon() },
                    discountTittle = value
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
                    text = "Tóm tắt thanh toán ",
                    fontWeight = FontWeight.SemiBold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Giá",
                        color = MaterialTheme.colorScheme.outline
                    )
                    if (uiState.selectedCoupon != null) {
                        Text(
                            text = formatCurrency2(uiState.oldTotalPrice),
                            textDecoration = TextDecoration.LineThrough,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.padding(end = 16.dp))
                    }
                    Text(
                        text = formatCurrency2(uiState.totalPrice),
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
                        text = "Phí vận chuyển",
                        color = MaterialTheme.colorScheme.outline
                    )
                    if (uiState.selectedShipping != null) {
                        Text(
                            text = formatCurrency2(uiState.selectedShipping.price),
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Text(
                            text = formatCurrency2(0.0),
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
                        text = "Tổng",
                        color = MaterialTheme.colorScheme.outline
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = formatCurrency2(uiState.finalPrice),
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
                    enabled = uiState.isButtonEnabled,
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
                        text = "Thanh toán",
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
            innerPadding = PaddingValues(0.dp),
            uiState = DataDummy.checkoutUiState,
            onEditAddress = {},
            onChooseShipping = {},
            onChooseCoupon = {},
            onChoosePayment = {},
        )
    }
}