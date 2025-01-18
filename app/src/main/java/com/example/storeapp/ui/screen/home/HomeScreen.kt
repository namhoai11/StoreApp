package com.example.storeapp.ui.screen.home

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.storeapp.R
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.Banners
import com.example.storeapp.ui.component.CategoryList
import com.example.storeapp.ui.component.HomeTopAppBar
import com.example.storeapp.ui.component.ListItems
import com.example.storeapp.ui.component.LoadingBox
import com.example.storeapp.ui.component.SectionTitle
import com.example.storeapp.ui.component.StoreAppBottomNavigationBar
import com.example.storeapp.ui.navigation.NavigationDestination

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
    navController: NavController,
) {
    val homeUiState by viewModel.uiState.collectAsState()

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
                navigateCartScreen
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
                    title = selectedCategory?.title ?: "Recommended",
                    actionText = "See All",
                    navigate = { navigateAllProduct() }
                )
            }

            item {
                if (homeUiState.showRecommenedLoading) {
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