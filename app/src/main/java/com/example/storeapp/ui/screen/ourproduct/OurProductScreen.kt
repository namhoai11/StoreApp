package com.example.storeapp.ui.screen.ourproduct


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.storeapp.R
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.user.ListItemsFullSize
import com.example.storeapp.ui.component.user.LoadingBox
import com.example.storeapp.ui.component.user.SearchBar
import com.example.storeapp.ui.component.user.StoreAppBottomNavigationBar
import com.example.storeapp.ui.navigation.NavigationDestination

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
            SearchBar(onSearch = { viewModel.searchItemsByName(it) })
        },
        bottomBar = {
            StoreAppBottomNavigationBar(
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
                if (ourProductUiState.showProductsLoading) {
                    LoadingBox(height = 200.dp)
                } else {
                    ListItemsFullSize(
                        items = if (ourProductUiState.currentQuery.isNotBlank()) {
                            ourProductUiState.productsSearched
                        } else {
                            ourProductUiState.allProducts
                        },
                        navigateToItemDetail = navigateProductDetails
                    )
                }
            }
        }
    }
}

