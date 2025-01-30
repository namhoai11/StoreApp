package com.example.storeapp.ui.screen.admin.manage.orders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.storeapp.R
import com.example.storeapp.data.local.DataDummy
import com.example.storeapp.data.local.OrderStatusProvider
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
) {
    Scaffold(
        topBar = {
            AdminTopAppBar(
                R.drawable.arrowback,
                "Quản lý",
                "Đơn hàng",
                { navController.navigateUp() },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 48.dp)
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
        OrderManagementContent(innerPadding = innerPadding)
    }
}

@Composable
fun OrderManagementContent(
    innerPadding: PaddingValues,
) {
    Column(
        modifier = Modifier.padding(innerPadding)
    ) {
        AdminSearch(
            textSearch = "Tìm kiếm đơn hàng",
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )
        FilterList(filterList = OrderStatusProvider.orderStatusList, onFilterSelected = {})
        OrderManagementList(listOrder = DataDummy.listOrder)
    }
}

@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewOrderManagementScreen() {
    StoreAppTheme {
        OrderManagementScreen(
            navController = rememberNavController()
        )
    }
}