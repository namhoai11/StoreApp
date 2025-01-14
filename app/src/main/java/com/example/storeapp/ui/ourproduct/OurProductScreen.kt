package com.example.storeapp.ui.ourproduct


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.storeapp.R
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.ListItemsFullSize
import com.example.storeapp.ui.component.LoadingBox
import com.example.storeapp.ui.component.SearchBar
import com.example.storeapp.ui.component.StoreAppBottomNavigationBar
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.navigation.NavigationItem
import com.example.storeapp.ui.screen.home.HomeDestination

object OurProductDestination : NavigationDestination {
    override val route = "ourproduct"
    override val titleRes = R.string.our_product
}

@Composable
fun OurProductScreen(
    navigateProductDetails: (Int) -> Unit,
    navController: NavController,
    viewModel: OurProductViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val ourProductUiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            SearchBar()
        },
        bottomBar = {
            StoreAppBottomNavigationBar(
                navigationItemList = listOf(
                    NavigationItem(
                        title = stringResource(id = HomeDestination.titleRes),
                        icon = painterResource(R.drawable.icon_home_outlined),
                        iconActive = painterResource(R.drawable.icon_home_filled),
                        route = HomeDestination.route
                    ),
                    NavigationItem(
                        title = stringResource(id = OurProductDestination.titleRes),
                        icon = painterResource(R.drawable.icon_shopping_store_outlined),
                        iconActive = painterResource(R.drawable.icon_shopping_store_filled),
                        route = OurProductDestination.route
                    ),
                    NavigationItem(
                        title = "Favorite",
                        icon = painterResource(R.drawable.icon_favourite_outlined),
                        iconActive = painterResource(R.drawable.icon_favourite_filled),
                        route = "favorite"
                    ),
                    NavigationItem(
                        title = "Orders",
                        icon = painterResource(R.drawable.icon_orders_outlined),
                        iconActive = painterResource(R.drawable.icon_orders_filled),
                        route = "orders"
                    ),
                    NavigationItem(
                        title = "Profile",
                        icon = painterResource(R.drawable.icon_profile_outlined),
                        iconActive = painterResource(R.drawable.icon_profile_filled),
                        route = "profile"
                    )
                ),
                navController = navController,
                currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                if (ourProductUiState.showItemsLoading) {
                    LoadingBox(height = 200.dp)
                } else {
                    ListItemsFullSize(
                        items = ourProductUiState.allItems,
                        navigateToItemDetail = navigateProductDetails
                    )
                }
            }
        }

    }
}

