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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.storeapp.R
import com.example.storeapp.ui.component.user.OrderStatus
import com.example.storeapp.ui.component.admin.AdminBottomNavigationBar
import com.example.storeapp.ui.component.admin.AdminTopAppBar
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.theme.StoreAppTheme
import java.text.DecimalFormat

object DashboardAdminDestination : NavigationDestination {
    override val route = "dashboard"
    override val titleRes = R.string.dashboard_title
}

@Composable
fun DashBoardScreen(
    navController: NavController,
    navigateUserApp: () -> Unit
) {
    Scaffold(
        topBar = {
            AdminTopAppBar(
                R.drawable.icon_navhome,
                "Admin",
                "LaHoaiNam",
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
        DashBoardContent(innerPadding = innerPadding)

    }
}

@Composable
fun DashBoardContent(
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
        DashboardCard(modifier = Modifier.padding(16.dp))
        OrderDashboard(modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun DashboardCard(
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
                                text = "27",
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
                            text = "Đơn mới mỗi ngày",
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
                                text = "27",
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
                                text = formatCurrency2(12345678.0),
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
                                text = formatCurrency2(12345678.0),
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
            }
        }
    }
}

@Composable
fun OrderDashboardHead() {

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
            modifier = Modifier.clickable {}
        )
    }
}

@Composable
fun OrderDashboard(
    modifier: Modifier = Modifier
) {
    val listString = listOf(
        "#TECH034",
        "#TECH034",
        "#TECH034",
        "#TECH034",
        "#TECH034",
        "#TECH034",
    )
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Cyan
        ),
        modifier = modifier
    ) {
        Column {
            OrderDashboardHead()
            LazyColumn(
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                items(listString) { item ->
                    OrderDashBoardItem(orderCode = item)

                }
            }
        }
    }

}

@Composable
fun OrderDashBoardItem(
    orderCode: String
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
            Column {
                Text(
                    text = orderCode.uppercase(),
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "24-10-2024 15:00",
                    color = Color.Gray,
//                            fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
            Column {
                Text(
                    text = "6 sản phẩm",
                    color = Color.Black,
                    fontSize = 14.sp,
                )
                Text(
                    text = formatCurrency2(5500000.0),
                    color = Color.Black,
                    fontSize = 14.sp,
//                    modifier = Modifier.alignByBaseline() // Canh theo baseline
                )

            }
//            OrderStatus(status = "Đang xử lý")
        }
//        Spacer(modifier = Modifier.height(8.dp))

    }
}

@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDashBoardScreen() {
    StoreAppTheme {
        DashBoardScreen(
            navController = rememberNavController(),
            {}
        )
    }
}

@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDashBoardContent() {
    StoreAppTheme {
        DashBoardContent(innerPadding = PaddingValues(0.dp))
    }
}

@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewOrderDashboard() {

    StoreAppTheme {
        OrderDashboard()
    }
}

@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewOrderDashBoardItem() {
    StoreAppTheme {
        OrderDashBoardItem("#TECH034")
    }
}


@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDashboardCard() {
    StoreAppTheme {
        DashboardCard()
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

//fun formatCurrency(amount: Double): String {
//    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
//    return formatter.format(amount)
//}

fun formatCurrency2(amount: Double): String {
    val formatter = DecimalFormat("#,###")
    return formatter.format(amount) + " ₫"
}