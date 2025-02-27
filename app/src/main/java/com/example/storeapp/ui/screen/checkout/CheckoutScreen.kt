package com.example.storeapp.ui.screen.checkout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.material3.ModalBottomSheet
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
import com.example.storeapp.model.CouponModel
import com.example.storeapp.model.CouponType
import com.example.storeapp.model.ShippingModel
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.function.formatCurrency2
import com.example.storeapp.ui.component.user.AddressItemScreen
import com.example.storeapp.ui.component.user.CartMiniList
import com.example.storeapp.ui.component.user.ConfirmDialog
import com.example.storeapp.ui.component.user.CouponInactiveSelected
import com.example.storeapp.ui.component.user.CouponItem
import com.example.storeapp.ui.component.user.CouponItemSelected
import com.example.storeapp.ui.component.user.ShippingItem
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.theme.StoreAppTheme


object CheckoutDestination : NavigationDestination {
    override val route = "checkout?locationId={locationId}"
    override val titleRes = R.string.checkout_title
    fun createRoute(locationId: String?): String {
        return "checkout?locationId=$locationId"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    onNavigateChooseAddress: () -> Unit,
    onNavigateToPayment: (String) -> Unit,
    viewModel: CheckoutViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isChooseShipping) {
        BottomSheetShipping(
            onDismiss = { viewModel.onConfirmationShipping() },
            state = uiState.listShipping,
            selectedShipping = uiState.selectedShipping,
            onChoose = { viewModel.shippingSelected(it) },
            onConfirmationShipping = { viewModel.onConfirmationShipping() }
        )
    }
    if (uiState.isChooseCoupon) {
        BottomSheetCoupon(
            onDismiss = { viewModel.onConfirmationCoupon() },
            state = uiState.listCoupon,
            selectedCoupon = uiState.selectedCoupon,
            onChoose = { viewModel.couponSelected(it) },
            onConfirmationCoupon = { viewModel.onConfirmationCoupon() }
        )
    }

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
            onEditAddress = { onNavigateChooseAddress() },
            onChooseShipping = { viewModel.onChooseShipping() },
            onChooseCoupon = { viewModel.onChooseCoupon() },
            onChoosePayment = { viewModel.onChoosePayment() }
        )
        if (uiState.isShowDialog) {
            ConfirmDialog(
                onDismiss = { viewModel.dismissDialog() },
                title = "Xác nhận",
                message = "Xác nhận tới thanh toán",
                confirmRemove = {
                    viewModel.confirmChoosePaymentClicked {
                        onNavigateToPayment(it)
                    }
                })
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
                        item = uiState.selectedShipping,
                        isChoose = true,
                        onChoose = { onChooseShipping() },
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
                    if (uiState.selectedCoupon != null && uiState.selectedCoupon.type != CouponType.FREE_SHIPPING) {
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
                        if (uiState.selectedCoupon != null && uiState.selectedCoupon.type == CouponType.FREE_SHIPPING) {
                            Text(
                                text = formatCurrency2(uiState.selectedShipping.price),
                                textDecoration = TextDecoration.LineThrough,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.padding(end = 16.dp))
                            Text(
                                text = formatCurrency2(0.0),
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Text(
                                text = formatCurrency2(uiState.selectedShipping.price),
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetCoupon(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    state: List<CouponModel>,
    selectedCoupon: CouponModel?,
    onChoose: (CouponModel) -> Unit,
    onConfirmationCoupon: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        windowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        BottomSheetCouponContent(
            modifier = Modifier
                .navigationBarsPadding()
                .wrapContentHeight(),
            state = state,
            couponSelected = selectedCoupon,
            onChoose = { onChoose(it) },
            onConfirmationCoupon = onConfirmationCoupon
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetShipping(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    state: List<ShippingModel>,
    selectedShipping: ShippingModel?,
    onChoose: (ShippingModel) -> Unit,
    onConfirmationShipping: () -> Unit
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
        windowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        BottomSheetShippingContent(
            modifier = Modifier
                .navigationBarsPadding()
                .wrapContentHeight(),
            state = state,
            shippingSelected = selectedShipping,
            onChoose = { onChoose(it) },
            onConfirmationShipping = onConfirmationShipping
        )
    }
}

@Composable
fun BottomSheetCouponContent(
    modifier: Modifier = Modifier,
    state: List<CouponModel>,
    couponSelected: CouponModel?,
    onChoose: (CouponModel) -> Unit,
    onConfirmationCoupon: () -> Unit
) {

    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.background
            )
    ) {
        Column(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 8.dp
                )
                .background(MaterialTheme.colorScheme.background)
                .align(Alignment.BottomCenter)
        ) {
            LazyColumn {
                itemsIndexed(items = state) { _, coupon ->
                    CouponItem(
                        item = coupon,
                        isChoose = couponSelected == coupon,
                        onChoose = { onChoose(coupon) },
                    )
                }
            }
            Button(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .height(55.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                enabled = couponSelected != null,
                onClick = {
                    onConfirmationCoupon()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Confirmation",
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun BottomSheetShippingContent(
    modifier: Modifier = Modifier,
    state: List<ShippingModel>,
    shippingSelected: ShippingModel?,
    onChoose: (ShippingModel) -> Unit,
    onConfirmationShipping: () -> Unit,
) {

    Box(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 8.dp
                )
                .background(MaterialTheme.colorScheme.background)
                .align(Alignment.BottomCenter)
        ) {
            LazyColumn {
                itemsIndexed(items = state) { _, shipping ->
                    ShippingItem(
                        item = shipping,
                        isChoose = shipping == shippingSelected,
                        onChoose = { onChoose(shipping) },
                    )
                }
            }
            Button(
                modifier = Modifier
                    .height(55.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                onClick = {
//                    onChoose(temporarySelectedShippingId!!)
                    onConfirmationShipping()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Confirmation",
                    fontSize = 14.sp,
                    color = Color.White
                )
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

@Preview(showBackground = true)
@Composable
private fun BottomSheetCouponContentPreview() {
    StoreAppTheme(dynamicColor = false) {
        BottomSheetCouponContent(
            state = DataDummy.dummyCoupon,
            onChoose = {},
            onConfirmationCoupon = {},
            couponSelected = DataDummy.dummyCoupon[0]
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomSheetShippingContentPreview() {
    StoreAppTheme {
        BottomSheetShippingContent(
            state = DataDummy.dummyShipping,
            onChoose = {},
            onConfirmationShipping = {},
            shippingSelected = DataDummy.dummyShipping[0]
        )
    }
}
