package com.example.storeapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.storeapp.ui.ourproduct.OurProductDestination
import com.example.storeapp.ui.ourproduct.OurProductScreen
import com.example.storeapp.ui.screen.category.CategoryDestination
import com.example.storeapp.ui.screen.category.CategoryScreen
import com.example.storeapp.ui.screen.home.HomeDestination
import com.example.storeapp.ui.screen.home.HomeScreen
import com.example.storeapp.ui.screen.intro.IntroDestination
import com.example.storeapp.ui.screen.intro.IntroScreen
import com.example.storeapp.ui.screen.login.LoginDestination
import com.example.storeapp.ui.screen.login.LoginScreen
import com.example.storeapp.ui.screen.productdetails.ProductDetailsDestination
import com.example.storeapp.ui.screen.productdetails.ProductDetailsScreen

@Composable
fun StoreAppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = IntroDestination.route,
        modifier = modifier
    ) {
        composable(route = IntroDestination.route) {
            IntroScreen(
                onNavigateSignIn = { navController.navigate(LoginDestination.route) },
                onNavigateHome = {
                    navController.navigate(HomeDestination.route) {
                        popUpTo(IntroDestination.route) {
                            inclusive = true
                        } // xóa bỏ các màn hình cũ không cần thiết
                    }
                }
            )
        }
        composable(route = LoginDestination.route) {
            LoginScreen()
        }
        composable(route = HomeDestination.route) {

            HomeScreen(
                navigateCartScreen = { /*TODO*/ },
                navigateAllProduct = { navController.navigate(CategoryDestination.route) },
                navigateProductDetails = {
                    navController.navigate("${ProductDetailsDestination.route}/$it")
                },
                navigateNotification = { /*TODO*/ },
                navController = navController
            )
        }
        composable(route = CategoryDestination.route) {
            CategoryScreen(
                navController = navController,
                navigateProductDetails = {
                    navController.navigate("${ProductDetailsDestination.route}/$it")
                }
            )
        }
        composable(
            route = ProductDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ProductDetailsDestination.productDetailsIdArg) {
                type = NavType.IntType
            })
        ) {
            ProductDetailsScreen(
                navController = navController,
                onAddToCartClick = {},
                onCartClick = {}
            )
        }
        composable(
            route = OurProductDestination.route
        ) {
            OurProductScreen(
                navigateProductDetails = {
                    navController.navigate("${ProductDetailsDestination.route}/$it")
                },
                navController = navController
            )
        }

    }
}