package com.example.storeapp.ui.screen.admin.manage.orders.orderdetailsmanagement

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.storeapp.R
import com.example.storeapp.data.local.DataDummy
import com.example.storeapp.model.OrderStatus
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.admin.AdminTopAppBar
import com.example.storeapp.ui.component.function.formatCurrency2
import com.example.storeapp.ui.component.function.timestampToDateOnlyString
import com.example.storeapp.ui.component.user.CartMiniList
import com.example.storeapp.ui.component.user.ConfirmDialog
import com.example.storeapp.ui.component.user.OrderDetailsRow
import com.example.storeapp.ui.component.user.OrderStatus
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.theme.StoreAppTheme


object OrderDetailsManagementDestination : NavigationDestination {
    override val route = "orderdetailsmanagement?orderId={orderId}"
    override val titleRes = R.string.ordermanage_title
    fun createRoute(orderId: String?): String {
        return "orderdetailsmanagement?orderId=$orderId"
    }
}

@Composable
fun OrderDetailsManagementScreen(
    navController: NavController,
    viewModel: OrderDetailsManagementViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            AdminTopAppBar(
                R.drawable.arrowback,
                "Quản lý",
                "Đơn hàng",
                { navController.navigateUp() },
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 16.dp)
            )
        },
//        bottomBar = {
//            AdminBottomNavigationBar(
//                navController = navController,
//                currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
//
//            )
//        }
    ) { innerPadding ->
        OrderDetailsManagementContent(
            innerPadding = innerPadding,
            uiState = uiState,
            cancelOrderClicked = { viewModel.onChooseCanceledOrder() },
            buttonOrderClicked = { viewModel.onChooseCompletedOrder() }
        )
        if (uiState.isShowCanceledOrderDialog) {
            ConfirmDialog(
                onDismiss = { viewModel.dismissCanceledOrderDialog() },
                title = "Xác nhận",
                message = "Xác nhận hủy đơn hàng",
                confirmRemove = {
                    viewModel.cancelOrderClicked()
                }
            )
        }

        if (uiState.isShowCompletedOrderDialog) {
            ConfirmDialog(
                onDismiss = { viewModel.dismissCompletedOrderDialog() },
                title = "Xác nhận",
                message = uiState.currentButtonText,
                confirmRemove = {
                    viewModel.completedOrderClicked()
                }
            )
        }
    }
}

@Composable
fun OrderDetailsManagementContent(
//    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    uiState: OrderDetailsManagementUiState,
    cancelOrderClicked: () -> Unit = {},
    buttonOrderClicked: () -> Unit = {},
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        Column(
            modifier = Modifier
                .weight(1f) // Dùng weight để phần nội dung có thể cuộn được mà không che button
                .padding(8.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OrderDetailsRow(
                imageId = R.drawable.icon_order,
                orderText = "Mã",
                content = uiState.order.orderCode
            )
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(16.dp)
            )
            OrderDetailsRow(
                imageId = R.drawable.icon_calendar,
                orderText = "Ngày đặt:",
                content = timestampToDateOnlyString(uiState.order.createdAt)
            )
            OrderDetailsRow(
                imageId = R.drawable.icon_calendar,
                orderText = "Ngày nhận(dự kiến):",
                content = timestampToDateOnlyString(uiState.order.estimatedDeliveryDate)
            )
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(16.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
                ) {
                    Text(
                        text = "Tình trạng",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                OrderStatus(status = uiState.order.status)
            }
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(16.dp)
            )
            OrderDetailsRow(
                imageId = R.drawable.icon_user_filled,
                orderText = "Khách hàng:",
                content = (uiState.user?.lastName
                    ?: "Khách hàng không còn hoạt động") + " " + (uiState.user?.firstName ?: ""),
            )
            OrderDetailsRow(
                imageId = R.drawable.icon_email,
                orderText = "Email:",
                content = uiState.user?.email ?: "",
            )
            OrderDetailsRow(
                imageId = R.drawable.icon_telephone,
                orderText = "Điện thoại:",
                content = uiState.user?.phone ?: "",
            )
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(16.dp)
            )
            OrderDetailsRow(
                imageId = R.drawable.icon_pinlocation,
                orderText = "Địa chỉ:",
                content = uiState.order.address.street + " - " + uiState.order.address.ward + " - " + uiState.order.address.district + " - " + uiState.order.address.province,
            )
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(16.dp)
            )
            OrderDetailsRow(
                imageId = R.drawable.admin_iconproduct,
                orderText = "Sản phẩm:",
                content = "",
            )
            CartMiniList(products = uiState.order.products)
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "Giá trị đơn hàng",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = formatCurrency2(uiState.order.totalPrice),
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.Black,
                    )
                    if (
                        uiState.order.status != OrderStatus.AWAITING_PAYMENT
                        && uiState.order.status != OrderStatus.CANCELED
                        && uiState.order.status != OrderStatus.COMPLETED
                    ) {
                        Button(
                        enabled = (!uiState.isLoading),
                            modifier = Modifier
                                .padding(top = 8.dp, bottom = 8.dp)
                                .height(55.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            onClick = { buttonOrderClicked() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                text = uiState.currentButtonText,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }
                    if (uiState.order.status != OrderStatus.CANCELED && uiState.order.status != OrderStatus.COMPLETED) {
                        Button(
                            modifier = Modifier
                                .padding(top = 8.dp, bottom = 8.dp)
                                .height(55.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            enabled = (!uiState.isLoading),
                            onClick = { cancelOrderClicked() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red
                            )
                        ) {
                            Text(
                                text = "Hủy đơn hàng",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White // ⚪ Chữ trắng
                            )
                        }
                    }

                }
            }
        }
    }
}

@Preview("Light Mode", showBackground = true)
@Preview("Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OrderScreenPreview() {
    StoreAppTheme {
        OrderDetailsManagementContent(
//            modifier = Modifier,
            uiState = OrderDetailsManagementUiState(
                order = DataDummy.order
            ),
            innerPadding = PaddingValues(0.dp)
        )
    }
}
