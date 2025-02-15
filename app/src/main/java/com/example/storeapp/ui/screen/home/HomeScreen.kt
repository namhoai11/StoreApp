package com.example.storeapp.ui.screen.home

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.storeapp.R
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.user.Banners
import com.example.storeapp.ui.component.user.CategoryList
import com.example.storeapp.ui.component.user.HomeTopAppBar
import com.example.storeapp.ui.component.user.ListItems
import com.example.storeapp.ui.component.user.LoadingBox
import com.example.storeapp.ui.component.user.SectionTitle
import com.example.storeapp.ui.component.user.StoreAppBottomNavigationBar
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.screen.cart.CartViewModel

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.home_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateCartScreen: () -> Unit,
    navigateAllProduct: () -> Unit,
    navigateProductDetails: (Int) -> Unit,
    navigateNotification: () -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    cartViewModel: CartViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavController,
) {
    LaunchedEffect(Unit) {
        cartViewModel.resetCartUiState()
    }

    val homeUiState by viewModel.uiState.collectAsState()
    val currentUser by viewModel.user.observeAsState()

    val cartUiState by cartViewModel.uiState.collectAsState()
    Log.d("HomeScreen", "user: $currentUser")
    val bannersLog = homeUiState.banners
    val categoryLog = homeUiState.categories
    Log.d("HomeScreen", "banners: $bannersLog")
    Log.d("HomeScreen", "categories: $categoryLog")

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            HomeTopAppBar(
                scrollBehavior = scrollBehavior,// Truyền scrollBehavior vào đây
                navigateCartScreen,
                navigateNotification,
                userName = "${currentUser?.firstName} ${currentUser?.lastName}",
                cartQuantity = cartUiState.listProductOnCart.size
            )
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

            // Banner
            item {
                if (homeUiState.showBannerLoading) {
                    LoadingBox(height = 200.dp)
                } else {
                    Banners(homeUiState.banners)
                }
            }
            item {
                SectionTitle(title = "Category", actionText = "")
            }
            //Category
            item {
                if (homeUiState.showCategoryLoading) {
                    LoadingBox(height = 200.dp)
                } else {
                    CategoryList(
                        categories = homeUiState.categories,
                        onCategorySelected = { id ->
                            viewModel.selectCategory(
                                id.toInt()
                            )
                            Log.d("HomeScreenId", "selectCategory: $id")
                        }
                    )
                    val listItemsLog = homeUiState.categories
                    Log.d("HomeScreen", "CurrentListItems: $listItemsLog")
                }
            }

            item {
                val selectedCategory =
                    homeUiState.categories.find { it.id == homeUiState.currentCategoryId }
                SectionTitle(
                    title = selectedCategory?.name ?: "Recommended",
                    actionText = "See All",
                    navigate = { navigateAllProduct() }
                )
            }

            item {
                if (homeUiState.showRecommendedLoading) {
                    LoadingBox(height = 200.dp)
                } else {
                    ListItems(
                        items = homeUiState.currentListItems,
                        navigateToItemDetails = navigateProductDetails
                    )
                }
            }
        }

    }
}