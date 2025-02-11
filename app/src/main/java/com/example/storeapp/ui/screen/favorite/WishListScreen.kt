package com.example.storeapp.ui.screen.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.storeapp.R
import com.example.storeapp.model.WishListModel
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.user.FavoriteList
import com.example.storeapp.ui.component.user.LoadingBox
import com.example.storeapp.ui.component.user.StoreAppBottomNavigationBar
import com.example.storeapp.ui.navigation.NavigationDestination

object FavoriteDestination : NavigationDestination {
    override val route = "favorite"
    override val titleRes = R.string.favorite_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishListScreen(
    navController: NavController,
    navigateProductDetails: (Int) -> Unit,
    viewModel: WishListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadUser() // Load lại dữ liệu khi quay lại màn hình
    }

//    val favItems = arrayListOf(
//        WishListModel(
//            productId = "1",
//            productName = "Business Laptop",
//            productCategory = "Electronics",
//            productImage = "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_1.png?alt=media&token=fb49a7c9-3094-4f5c-9ea6-b8365cd86323",
//            productPrice = 550.0,
//            productQuantity = 2
//        ),
//        WishListModel(
//            productId = "2",
//            productName = "Gaming Laptop",
//            productCategory = "Electronics",
//            productImage = "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_2.png?alt=media&token=3f826014-4808-4387-af6f-22dc7ddd4780",
//            productPrice = 1200.0,
//            productQuantity = 2
//        )
//    )
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(horizontal = 16.dp),
                title = {
                    Text(
                        text = "Favorite",
//                        fontFamily = poppinsFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
//                navigationIcon = {
//                    Icon(
//                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                        contentDescription = "back",
//                        modifier = Modifier.clickable { navController.navigateUp() }
//                    )
//                },
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
        FavContent(
            innerPadding = innerPadding,
            favItems = uiState.listItems,
            isShowLoading = uiState.showWishListLoading,
            onFavIconClick = {
                viewModel.favoriteClick(it)
            },
            onFavItemClick = {
                navigateProductDetails(it)
            }
        )
    }

}

@Composable
fun FavContent(
    innerPadding: PaddingValues,
    favItems: List<WishListModel>,
    isShowLoading: Boolean = false,
    onFavIconClick: (String) -> Unit = {},
    onFavItemClick: (Int) -> Unit = {},

    ) {
    if (isShowLoading) {
        LoadingBox(height = 200.dp)
    } else {
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if (favItems.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .height(100.dp)
                    ) {
                        Text(
                            text = "Wishlist Is Empty",
                            modifier = Modifier.align(Alignment.Center),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                item {
                    FavoriteList(favItems, onFavIconClick, onFavItemClick)
                }
            }
        }
    }
}