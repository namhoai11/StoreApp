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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.storeapp.R
import com.example.storeapp.data.local.DataDummy
import com.example.storeapp.model.CartModel
import com.example.storeapp.ui.component.user.CartList
import com.example.storeapp.ui.component.user.CartSummary
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.theme.StoreAppTheme

object CartDestination : NavigationDestination {
    override val route = "cart"
    override val titleRes = R.string.cart_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController
) {
    val cart = DataDummy.cartItems
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
        CartContent(innerPadding = innerPadding, cartItems = cart)
    }
}

@Composable
fun CartContent(
    innerPadding: PaddingValues,
    cartItems: CartModel,
) {
    LazyColumn(
        contentPadding = innerPadding,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        if (cartItems.products.isEmpty()) {
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
                CartList(cartItems = cartItems)
            }
            item {
                CartSummary(465.0, 12.9, 10.0)
            }
        }
    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CartContentPreview() {
    StoreAppTheme {
val cart = DataDummy.cartItems
        CartContent(
            PaddingValues(0.dp),
            cart
        )
    }
}