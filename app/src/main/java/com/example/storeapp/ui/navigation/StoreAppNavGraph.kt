package com.example.storeapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.storeapp.ui.screen.cart.CartDestination
import com.example.storeapp.ui.screen.cart.CartScreen
import com.example.storeapp.ui.screen.admin.dashboard.DashBoardScreen
import com.example.storeapp.ui.screen.admin.dashboard.DashboardAdminDestination
import com.example.storeapp.ui.screen.admin.manage.ManageScreen
import com.example.storeapp.ui.screen.admin.manage.ManegeAdminDestination
import com.example.storeapp.ui.screen.favorite.FavoriteDestination
import com.example.storeapp.ui.screen.favorite.FavoriteScreen
import com.example.storeapp.ui.screen.ourproduct.OurProductDestination
import com.example.storeapp.ui.screen.ourproduct.OurProductScreen
import com.example.storeapp.ui.screen.category.CategoryDestination
import com.example.storeapp.ui.screen.category.CategoryScreen
import com.example.storeapp.ui.screen.home.HomeDestination
import com.example.storeapp.ui.screen.home.HomeScreen
import com.example.storeapp.ui.screen.intro.IntroDestination
import com.example.storeapp.ui.screen.intro.IntroScreen
import com.example.storeapp.ui.screen.login.LoginDestination
import com.example.storeapp.ui.screen.login.LoginScreen
import com.example.storeapp.ui.screen.notification.NotificateDestination
import com.example.storeapp.ui.screen.notification.NotificationScreen
import com.example.storeapp.ui.screen.order.OrdersDestination
import com.example.storeapp.ui.screen.order.OrdersScreen
import com.example.storeapp.ui.screen.productdetails.ProductDetailsDestination
import com.example.storeapp.ui.screen.productdetails.ProductDetailsScreen
import com.example.storeapp.ui.screen.profile.ProfileDestination
import com.example.storeapp.ui.screen.profile.ProfileScreen

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
                navigateCartScreen = { navController.navigate(CartDestination.route) },
                navigateAllProduct = { navController.navigate(CategoryDestination.route) },
                navigateProductDetails = {
                    navController.navigate("${ProductDetailsDestination.route}/$it")
                },
                navigateNotification = { navController.navigate(NotificateDestination.route) },
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
        composable(
            CartDestination.route
        ) {
            CartScreen(navController = navController)
        }

        composable(
            FavoriteDestination.route
        ) {
            FavoriteScreen(navController = navController)
        }

        composable(
            NotificateDestination.route
        ) {
            NotificationScreen(navController = navController)
        }

        composable(OrdersDestination.route) {
            OrdersScreen(navController = navController)
        }
        composable(ProfileDestination.route) {
            ProfileScreen(navController)
        }
        composable(DashboardAdminDestination.route) {
            DashBoardScreen(navController = navController,
                navigateUserApp = {
                    navController.navigate(ProfileDestination.route)
                })
        }
        composable(ManegeAdminDestination.route) {
            ManageScreen(navController = navController,
                navigateUserApp = {
                    navController.navigate(ProfileDestination.route)
                })
        }
    }
}