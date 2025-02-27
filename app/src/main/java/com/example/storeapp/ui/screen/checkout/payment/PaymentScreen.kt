package com.example.storeapp.ui.screen.checkout.payment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.storeapp.R
import com.example.storeapp.data.local.DataDummy
import com.example.storeapp.model.OrderModel
import com.example.storeapp.model.PaymentMethodModel
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.function.formatCurrency2
import com.example.storeapp.ui.component.user.ConfirmDialog
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.theme.StoreAppTheme

object PaymentDestination : NavigationDestination {
    override val route = "payment?orderId={orderId}"
    override val titleRes = R.string.paymentmethod_title
    fun createRoute(orderId: String?): String {
        return "payment?orderId=$orderId"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onNavigateToSuccessPayment: (String) -> Unit,
    viewModel: PaymentViewModel = viewModel(factory = AppViewModelProvider.Factory)
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
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            PaymentContent(
                uiState = uiState,
                onChoosePaymentMethod = {
                    viewModel.onChoosePaymentMethod()
                },
                onChooseCardPayment = {
                    viewModel.onChooseCardPayment(it)
                }
            )
            if (uiState.isShowDialog) {
                ConfirmDialog(
                    onDismiss = { viewModel.dismissDialog() },
                    title = "Xác nhận",
                    message = "Xác nhận thanh toán",
                    confirmRemove = {
                        viewModel.confirmPaymentClicked {
                            onNavigateToSuccessPayment(it)
                        }
                    })
            }
        }
    }
}

@Composable
fun PaymentContent(
    modifier: Modifier = Modifier,
    uiState: PaymentUiState,
    onChoosePaymentMethod: () -> Unit,
    onChooseCardPayment: (PaymentMethodModel) -> Unit
) {
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        visible = true
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .padding(
                    vertical = 8.dp,
                    horizontal = 16.dp
                )
                .fillMaxSize()
                .weight(1f)
        ) {
            LazyColumn(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
            ) {
                itemsIndexed(items = uiState.listPaymentMethodModel) { _, payment ->
                    AnimatedVisibility(
                        modifier = Modifier.animateItem(
                            fadeInSpec = null,
                            fadeOutSpec = null,
                            placementSpec = tween(100)
                        ),
                        visible = visible,
                        enter = slideInVertically(
                            initialOffsetY = { -it },
                            animationSpec = tween(durationMillis = 300)
                        ) + fadeIn(animationSpec = tween(durationMillis = 300))
                    ) {
                        CardPaymentItem(
                            paymentMethod = payment,
                            isChoose = uiState.paymentMethodSelected == payment,
                            onChoose = {
                                onChooseCardPayment(payment)
                            }
                        )
                    }
                }
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
                    text = "Tổng đơn hàng",
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
                    Text(
                        text = uiState.currentOrder?.let { formatCurrency2(it.totalPrice) } ?: "",
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Button(
                    enabled = uiState.isButtonEnabled,
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                        .height(55.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    onClick = onChoosePaymentMethod,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Thanh toán ngay",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun CardPaymentItem(
    modifier: Modifier = Modifier,
    paymentMethod: PaymentMethodModel,
    isChoose: Boolean,
    onChoose: () -> Unit
) {
    Card(
        border = BorderStroke(
            width = 1.dp,
            color = Color.LightGray
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clickable { onChoose() }
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(paymentMethod.icon),
                contentDescription = paymentMethod.name,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(
                        width = 100.dp,
                        height = 60.dp
                    )
            )
            Spacer(modifier = Modifier.weight(1f))
            RadioButton(
                selected = isChoose,
                onClick = onChoose
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CardPaymentItemPreview() {
    StoreAppTheme(dynamicColor = false) {
        CardPaymentItem(
            paymentMethod = PaymentMethodModel(
                icon = R.drawable.icon_discover,
                name = "Discover"
            ),
            isChoose = true,
            onChoose = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PaymentScreenPreview() {
    StoreAppTheme(dynamicColor = false) {
        PaymentContent(
            uiState = PaymentUiState(
                listPaymentMethodModel = DataDummy.dummyPaymentMethod,
                paymentMethodSelected = PaymentMethodModel(
                    icon = R.drawable.icon_discover,
                    name = "Discover"
                ),
                currentOrder = OrderModel(),
            ),
            onChoosePaymentMethod = {},
            onChooseCardPayment = {}
        )
    }
}