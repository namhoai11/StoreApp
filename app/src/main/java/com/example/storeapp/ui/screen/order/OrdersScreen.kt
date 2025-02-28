package com.example.storeapp.ui.screen.order

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.storeapp.R
import com.example.storeapp.data.local.DataDummy
import com.example.storeapp.model.OrderModel
import com.example.storeapp.model.OrderStatus
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.user.FilterOrder
import com.example.storeapp.ui.component.user.OrderList
import com.example.storeapp.ui.component.user.SearchOrder
import com.example.storeapp.ui.component.user.StoreAppBottomNavigationBar
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.theme.StoreAppTheme

object OrdersDestination : NavigationDestination {
    override val route = "order"
    override val titleRes = R.string.orders_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    navController: NavController,
    onNavigateOrderDetails:(String)->Unit,
    viewModel: OrderViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
//                modifier = Modifier.padding(horizontal = 16.dp),
                title = {
                    Text(
                        text = stringResource(id = R.string.orders_title),
//                        fontFamily = poppinsFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back",
                        modifier = Modifier.padding(start = 16.dp)
                            .clickable { navController.navigateUp() }
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            StoreAppBottomNavigationBar(
                navController = navController,
                currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
            )
        }
    ) { innerPadding ->
        OrderContent(
            uiState = uiState,
            innerPadding = innerPadding,
            onSearchOrder = { viewModel.searchOrdersByCode(it) },
            onOrderStatusSelected = { viewModel.selectOrderStatus(it) },
            onOrderItemClick = {onNavigateOrderDetails(it.orderCode)}
        )
    }
}

@Composable
fun OrderContent(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    uiState: OrderUiState,
    onOrderStatusSelected: (OrderStatus) -> Unit = {},
    onOrderItemClick: (OrderModel) -> Unit = {},
    onSearchOrder: (String) -> Unit = {}
) {
    LazyColumn(
        contentPadding = innerPadding,
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    modifier = modifier.weight(1f)
                ) {
                    Text(
                        text = "Tình trạng",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    FilterOrder(
                        modifier = modifier,
                        onOptionSelected = {
                            onOrderStatusSelected(it)
                        }
                    )
                }
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Tìm đơn hàng",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    SearchOrder(onSearch = { onSearchOrder(it) })
                }

            }
        }
        item {
            OrderList(
                modifier = modifier.padding(top = 32.dp),
                orderList = if (uiState.currentQuery.isNotBlank()) {
                    uiState.ordersSearched
                } else {
                    uiState.currentListOrder
                },
                orderItemClick = { onOrderItemClick(it) }
            )
        }

    }

}

@Preview("Light Mode", showBackground = true)
@Preview("Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OrderScreenPreview() {
    StoreAppTheme {
        OrderContent(
            modifier = Modifier,
            uiState = DataDummy.orderUiState,
            innerPadding = PaddingValues(0.dp)
        )
    }
}