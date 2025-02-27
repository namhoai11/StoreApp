package com.example.storeapp.ui.screen.cart

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.storeapp.R
import com.example.storeapp.data.local.DataDummy
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.user.CartList
import com.example.storeapp.ui.component.user.CartSummary
import com.example.storeapp.ui.component.user.ConfirmDialog
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.theme.StoreAppTheme

object CartDestination : NavigationDestination {
    override val route = "cart"
    override val titleRes = R.string.cart_title
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    onNavigateProductDetails: (String) -> Unit,
    onNavigateToCheckout: () -> Unit,
    viewModel: CartViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
//    val cart = DataDummy.cartItems
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Your Cart",
//                        fontFamily = poppinsFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back",
                        modifier = Modifier
                            .clickable { navController.navigateUp() }
                            .padding(horizontal = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        CartContent(
            increaseClick = { viewModel.increaseClicked(it) },
            decreaseClick = { viewModel.decreaseClicked(it) },
            removeClick = { viewModel.onRemoveClick(it) },
            onCartItemClicked = { onNavigateProductDetails(it) },
            innerPadding = innerPadding,
            cartItems = uiState.listProductOnCart,
            totalPrice = uiState.totalPrice,
            checkoutClick = {
                onNavigateToCheckout()
            }
        )
        if (uiState.isShowConfirmRemovedDialog) {
            ConfirmDialog(
                onDismiss = { viewModel.dismissRemoveDialog() },
                title = "Xóa Sản phẩm",
                message = "Xác nhận xóa sản phẩm",
                confirmRemove = { viewModel.confirmRemoveClicked() })
        }
    }
}

@Composable
fun CartContent(
    increaseClick: (ProductsOnCartToShow) -> Unit,
    decreaseClick: (ProductsOnCartToShow) -> Unit,
    removeClick: (ProductsOnCartToShow) -> Unit,
    onCartItemClicked: (String) -> Unit = {},
    innerPadding: PaddingValues,
    cartItems: List<ProductsOnCartToShow>,
    totalPrice: Double = 0.0,
    checkoutClick: () -> Unit = {},
) {
    LazyColumn(
        contentPadding = innerPadding,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        if (cartItems.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .height(100.dp)
                ) {
                    Text(
                        text = "Cart Is Empty",
                        modifier = Modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            item {
                CartList(
                    increaseClick = { increaseClick(it) },
                    decreaseClick = { decreaseClick(it) },
                    removeClick = { removeClick(it) },
                    onCartItemClicked = { onCartItemClicked(it) },
                    cartItems = cartItems
                )
            }
            item {
                CartSummary(totalPrice, checkoutClick)
            }
        }
    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CartContentPreview() {
    StoreAppTheme {
        val cart = listOf(
            DataDummy.productsOnCartToShow,
            DataDummy.productsOnCartToShow,
            DataDummy.productsOnCartToShow,
            DataDummy.productsOnCartToShow,
            DataDummy.productsOnCartToShow
        )
        CartContent(
            {}, {}, {}, {},
            PaddingValues(0.dp),
            cart
        )
    }
}