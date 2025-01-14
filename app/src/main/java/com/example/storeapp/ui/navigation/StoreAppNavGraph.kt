package com.example.storeapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.storeapp.ui.screen.category.CategoryDestination
import com.example.storeapp.ui.screen.category.CategoryScreen
import com.example.storeapp.ui.screen.home.HomeDestination
import com.example.storeapp.ui.screen.home.HomeScreen
import com.example.storeapp.ui.screen.intro.IntroDestination
import com.example.storeapp.ui.screen.intro.IntroScreen
import com.example.storeapp.ui.screen.login.LoginDestination
import com.example.storeapp.ui.screen.login.LoginScreen

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
                onNavigateHome = { navController.navigate(HomeDestination.route) }
            )
        }
        composable(route = LoginDestination.route) {
            LoginScreen()
        }
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateCartScreen = { /*TODO*/ },
                navigateAllProduct = { navController.navigate(CategoryDestination.route) },
                navigateProdcutDetails = { /*TODO*/ },
                navigateNotification = { /*TODO*/ },
                navController = navController
            )
        }
        composable(route = CategoryDestination.route) {
            CategoryScreen(
                navcontroller = navController,
            )
        }
//        composable(
//            route = ContactEditDestination.routeWithArgs,
//            arguments = listOf(navArgument(ContactEditDestination.contactIdArg) {
//                type = NavType.IntType
//            })
//        ) {
//            ContactEditScreen(navigateBack = { navController.popBackStack() }, onNavigateUp = { navController.navigateUp() })
//        }
    }
}