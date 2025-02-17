package com.example.storeapp.ui.screen.checkout

import androidx.compose.runtime.Composable
import com.example.storeapp.R
import com.example.storeapp.ui.navigation.NavigationDestination

object CheckoutDestination : NavigationDestination {
    override val route = "checkout"
    override val titleRes = R.string.checkout_title
}

@Composable
fun Checkout(){

}