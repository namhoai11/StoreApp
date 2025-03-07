package com.example.storeapp.ui.screen.category

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.storeapp.R
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.user.CategoryList
import com.example.storeapp.ui.component.user.ListItemsFullSize
import com.example.storeapp.ui.component.user.LoadingBox
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.screen.home.HomeViewModel

object CategoryDestination : NavigationDestination {
    override val route = "category"
    override val titleRes = R.string.category_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    navController: NavController,
    navigateProductDetails: (String) -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val homeUiState by viewModel.uiState.collectAsState()
//    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
//        snackbarHost = {
//            SnackbarHost(hostState = snackbarHostState) {
//                Snackbar(
//                    modifier = Modifier.padding(16.dp)
//                ) {
//                    Text(
//                        text = it.visuals.message,
//                    )
//                }
//            }
//        },
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(horizontal = 16.dp),
                title = {
                    val selectedCategory =
                        homeUiState.categories.find { it.id == homeUiState.currentCategoryId }
                    Text(
                        text = selectedCategory?.name ?: "All",
//                        fontFamily = poppinsFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back",
                        modifier = Modifier.clickable { navController.navigateUp() }
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (homeUiState.showCategoryLoading) {
                LoadingBox(height = 200.dp)
            } else {
                CategoryList(
                    categories = homeUiState.categories,
                    onCategorySelected = { id ->
                        viewModel.selectCategory(
                            id
                        )
                        Log.d("HomeScreenId", "selectCategory: $id")
                    },
                    isShowRecommend = false
                )
                val listItemsLog = homeUiState.categories
                Log.d("HomeScreen", "CurrentListItems: $listItemsLog")
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    if (homeUiState.showRecommendedLoading) {
                        LoadingBox(height = 200.dp)
                    } else {
                        ListItemsFullSize(
                            items = homeUiState.currentListItems,
                            navigateToItemDetail = navigateProductDetails
                        )
                    }
                }
            }
        }
    }
}