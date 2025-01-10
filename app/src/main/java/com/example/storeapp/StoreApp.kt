package com.example.storeapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.storeapp.ui.navigation.StoreAppNavHost

@Composable
fun StoreApp(navController: NavHostController = rememberNavController()) {
    StoreAppNavHost(navController = navController)
}