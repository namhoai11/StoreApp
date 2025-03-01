package com.example.storeapp.ui.screen.admin.manage.orders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.storeapp.R
import com.example.storeapp.data.local.DataDummy
import com.example.storeapp.model.OrderModel
import com.example.storeapp.model.OrderStatus
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.admin.AdminSearch
import com.example.storeapp.ui.component.admin.AdminTopAppBar
import com.example.storeapp.ui.component.admin.FilterList
import com.example.storeapp.ui.component.admin.OrderManagementList
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.theme.StoreAppTheme

object OrderManagementDestination : NavigationDestination {
    override val route = "ordermanagement"
    override val titleRes = R.string.ordermanage_title
}

@Composable
fun OrderManagementScreen(
    navController: NavController,
    onNavigateOrderManagementDetail: (String) -> Unit,
    viewModel: OrderManagementViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }
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
        OrderManagementContent(
            uiState = uiState,
            innerPadding = innerPadding,
            onOrderStatusSelected = { viewModel.selectOrderStatus(it) },
            onOrderItemClick = { onNavigateOrderManagementDetail(it.orderCode) },
            onSearchOrder = { viewModel.searchOrdersByCode(it) }
        )
    }
}

@Composable
fun OrderManagementContent(
    uiState: OrderManagementUiState,
    innerPadding: PaddingValues,
    onOrderStatusSelected: (OrderStatus) -> Unit = {},
    onOrderItemClick: (OrderModel) -> Unit = {},
    onSearchOrder: (String) -> Unit = {}
) {
    val filterList = OrderStatus.entries.map { it.toString() }
    Column(
        modifier = Modifier.padding(innerPadding)
    ) {
        AdminSearch(
            textSearch = "Tìm kiếm đơn hàng",
            onSearch = { onSearchOrder(it) },
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )
        FilterList(filterList = filterList, onFilterSelected = {
            onOrderStatusSelected(OrderStatus.fromString(it)!!)
        })
        OrderManagementList(
            if (uiState.currentQuery.isNotBlank()) {
                uiState.ordersSearched
            } else {
                uiState.currentListOrders
            },
            userMap = uiState.userMap,
            onOrderItemClick = { onOrderItemClick(it) }
        )
    }
}

@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewOrderManagementScreen() {
    StoreAppTheme {
        OrderManagementContent(
            uiState = OrderManagementUiState(
                allOrder = DataDummy.listOrder,
            ),
            innerPadding = PaddingValues(0.dp)
        )
    }
}