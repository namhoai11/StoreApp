package com.example.storeapp.ui.screen.admin.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.storeapp.R
import com.example.storeapp.data.local.DataDummy
import com.example.storeapp.model.OrderModel
import com.example.storeapp.model.Role
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.admin.AdminBottomNavigationBar
import com.example.storeapp.ui.component.admin.AdminTopAppBar
import com.example.storeapp.ui.component.function.formatCurrency2
import com.example.storeapp.ui.component.function.timestampToDateString
import com.example.storeapp.ui.component.user.OrderStatus
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.theme.StoreAppTheme

object DashboardAdminDestination : NavigationDestination {
    override val route = "dashboard"
    override val titleRes = R.string.dashboard_title
}

@Composable
fun DashBoardScreen(
    navController: NavController,
    navigateUserApp: () -> Unit,
    onNavigateToOrderManagement: () -> Unit = {},
    viewModel: DashBoardViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentUser by viewModel.user.observeAsState()
    val name = "${currentUser?.firstName ?: ""} ${currentUser?.lastName ?: ""}"

    LaunchedEffect(currentUser) {
        if (currentUser != null && currentUser!!.role != Role.ADMIN) {
            navigateUserApp()
        }
    }


    Scaffold(
        topBar = {
            AdminTopAppBar(
                R.drawable.icon_navhome,
                "Admin",
                name,
                navigateUserApp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 48.dp)
            )
        },
        bottomBar = {
            AdminBottomNavigationBar(
                navController = navController,
                currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

            )
        }
    ) { innerPadding ->
        DashBoardContent(
            innerPadding = innerPadding,
            uiState = uiState,
            onNavigateToOrderManagement = { onNavigateToOrderManagement() })

    }
}

@Composable
fun DashBoardContent(
    uiState: DashBoardUiState,
    onNavigateToOrderManagement: () -> Unit = {},
    innerPadding: PaddingValues,
) {
    Column(
        modifier = Modifier.padding(innerPadding)
    ) {
        Text(
            text = "Dashboard",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 16.dp)
        )
        DashboardCard(uiState = uiState, modifier = Modifier.padding(16.dp))
        OrderDashboard(modifier = Modifier.padding(16.dp),
            listOrder = uiState.listNewOrder,
            onNavigateToOrderManagement = { onNavigateToOrderManagement() })
    }
}

@Composable
fun DashboardCard(
    uiState: DashBoardUiState,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row() {
                Card(
                    modifier = Modifier.weight(1f),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(10.dp),
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically // Đảm bảo hàng canh giữa theo chiều dọc
                        ) {
                            Text(
                                text = "${uiState.listOrderOfMonth.size}",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                modifier = Modifier.alignByBaseline() // Canh theo baseline
                            )
                            Spacer(modifier = Modifier.width(4.dp)) // Tạo khoảng cách giữa 2 Text
                            Text(
                                text = "đơn",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.alignByBaseline() // Canh theo baseline
                            )
                        }
                        Text(
                            text = "Đơn tháng này",
                            color = Color.Gray,
//                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Card(
                    modifier = Modifier.weight(1f),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(10.dp),
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically // Đảm bảo hàng canh giữa theo chiều dọc
                        ) {
                            Text(
                                text = "${uiState.listOrderProcessing.size}",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                modifier = Modifier.alignByBaseline() // Canh theo baseline
                            )
                            Spacer(modifier = Modifier.width(4.dp)) // Tạo khoảng cách giữa 2 Text
                            Text(
                                text = "đơn",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.alignByBaseline() // Canh theo baseline
                            )
                        }
                        Text(
                            text = "Đơn đang xử lý",
                            color = Color.Gray,
//                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Card(
                    modifier = Modifier.weight(1f),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(10.dp),
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically // Đảm bảo hàng canh giữa theo chiều dọc
                        ) {
                            Text(
                                text = formatCurrency2(uiState.revenueThisMonth),
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                modifier = Modifier.alignByBaseline() // Canh theo baseline
                            )
//                            Spacer(modifier = Modifier.width(4.dp)) // Tạo khoảng cách giữa 2 Text
//                            Text(
//                                text = " ₫",
//                                color = Color.Black,
//                                fontWeight = FontWeight.Bold,
//                                fontSize = 12.sp,
//                                modifier = Modifier.alignByBaseline() // Canh theo baseline
//                            )
                        }
                        Text(
                            text = "Doanh thu tháng này",
                            color = Color.Gray,
//                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Card(
                    modifier = Modifier.weight(1f),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(10.dp),
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically // Đảm bảo hàng canh giữa theo chiều dọc
                        ) {
                            Text(
                                text = formatCurrency2(uiState.revenueThisDay),
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                modifier = Modifier.alignByBaseline() // Canh theo baseline
                            )
//                            Spacer(modifier = Modifier.width(4.dp)) // Tạo khoảng cách giữa 2 Text
//                            Text(
//                                text = " ₫",
//                                color = Color.Black,
//                                fontWeight = FontWeight.Bold,
//                                fontSize = 12.sp,
//                                modifier = Modifier.alignByBaseline() // Canh theo baseline
//                            )
                        }
                        Text(
                            text = "Doanh thu hôm nay",
                            color = Color.Gray,
//                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrderDashboardHead(
    onNavigateToOrderManagement: () -> Unit = {}
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Các đơn mới nhất",
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Xem tất cả",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable {
                onNavigateToOrderManagement()
            }
        )
    }
}

@Composable
fun OrderDashboard(
    modifier: Modifier = Modifier,
    listOrder: List<OrderModel>,
    onNavigateToOrderManagement: () -> Unit = {}

) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier
    ) {
        Column {
            OrderDashboardHead(onNavigateToOrderManagement)
            LazyColumn(
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                items(listOrder) { item ->
                    OrderDashBoardItem(orderItem = item)

                }
            }
        }
    }

}

@Composable
fun OrderDashBoardItem(
    orderItem: OrderModel
) {
    Column {
        HorizontalDivider(
            thickness = 2.dp,
            modifier = Modifier.padding(16.dp, 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = orderItem.orderCode,
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )
                Text(
                    text = timestampToDateString(orderItem.createdAt),
                    color = Color.Gray,
//                            fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
            Column(
                modifier = Modifier.weight(1f)

            ) {
                Text(
                    text = "${orderItem.products.sumOf { it.quantity }} sản phẩm",
                    color = Color.Black,
                    fontSize = 14.sp,
                )
                Text(
                    text = formatCurrency2(orderItem.totalPrice),
                    color = Color.Black,
                    fontSize = 14.sp,
//                    modifier = Modifier.alignByBaseline() // Canh theo baseline
                )

            }
            OrderStatus(
                modifier = Modifier.weight(1f),
                status = orderItem.status)
        }
//        Spacer(modifier = Modifier.height(8.dp))

    }
}

//@Preview("Light Theme", showBackground = true)
////@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun PreviewDashBoardScreen() {
//    StoreAppTheme {
//        DashBoardScreen(
//            navController = rememberNavController(),
//            {},
//            {}
//        )
//    }
//}

@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDashBoardContent() {
    StoreAppTheme {
        DashBoardContent(
            uiState = DashBoardUiState(
                allOrder = DataDummy.listOrder,
                listOrderProcessing = DataDummy.listOrder,
                listNewOrder = DataDummy.listOrder,
                listOrderOfMonth = DataDummy.listOrder,
                revenueThisDay = 140000.0,
                revenueThisMonth = 500000.0,
            ), innerPadding = PaddingValues(0.dp)
        )
    }
}

@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewOrderDashboard() {

    StoreAppTheme {
        OrderDashboard(listOrder = DataDummy.listOrder)
    }
}

@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewOrderDashBoardItem() {
    StoreAppTheme {
        OrderDashBoardItem(DataDummy.order)
    }
}


@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDashboardCard() {
    StoreAppTheme {
        DashboardCard(
            uiState = DashBoardUiState(
                allOrder = DataDummy.listOrder,
                listOrderProcessing = DataDummy.listOrder,
                listNewOrder = DataDummy.listOrder,
                listOrderOfMonth = DataDummy.listOrder,
                revenueThisDay = 140000.0,
                revenueThisMonth = 500000.0,
            ),
        )
    }
}

@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewOrderDashboardHead() {
    StoreAppTheme {
        OrderDashboardHead()
    }
}
