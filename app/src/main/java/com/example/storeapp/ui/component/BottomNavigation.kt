package com.example.storeapp.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.storeapp.R
import com.example.storeapp.ui.navigation.NavigationItem

@Composable
fun StoreAppBottomNavigationBar(
    navigationItemList: List<NavigationItem>,
    navController: NavController,
    currentRoute: String?,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    NavigationBar(modifier = modifier) {
        navigationItemList.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    if (currentRoute != navItem.route) {
                        navController.navigate(navItem.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Icon(
                            painter = if (currentRoute == navItem.route) navItem.iconActive else navItem.icon,
                            contentDescription = navItem.title,
                            tint = if (currentRoute == navItem.route) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp)) // Khoảng cách giữa icon và title
                        Text(
                            text = navItem.title,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (currentRoute == navItem.route) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
fun PreviewStoreAppBottomNavigationBar() {
    // Fake NavController for preview
    val fakeNavController = rememberNavController()
    val currentRoute = "product" // Set to a route from your list, e.g. "home"

    // Sample navigation items
    val navigationItems = listOf(
        NavigationItem(
            title = "Home",
            icon = painterResource(R.drawable.icon_home_outlined),
            iconActive = painterResource(R.drawable.icon_home_filled),
            route = "home"
        ),
        NavigationItem(
            title = "Product",
            icon = painterResource(R.drawable.icon_shopping_store_outlined),
            iconActive = painterResource(R.drawable.icon_shopping_store_filled),
            route = "product"
        ),
//        NavigationItem(
//            title = "Cart",
//            icon = painterResource(R.drawable.icon_cart_outlined),
//            iconActive = painterResource(R.drawable.icon_cart_filled),
//            route = "cart"
//        ),
        NavigationItem(
            title = "Favorite",
            icon = painterResource(R.drawable.icon_favourite_outlined),
            iconActive = painterResource(R.drawable.icon_favourite_filled),
            route = "favorite"
        ),
        NavigationItem(
            title = "Orders",
            icon = painterResource(R.drawable.icon_orders_outlined),
            iconActive = painterResource(R.drawable.icon_orders_filled),
            route = "orders"
        ),

        NavigationItem(
            title = "Profile",
            icon = painterResource(R.drawable.icon_profile_outlined),
            iconActive = painterResource(R.drawable.icon_profile_filled),
            route = "profile"
        )
    )

    StoreAppBottomNavigationBar(
        navigationItemList = navigationItems,
        navController = fakeNavController,
        currentRoute = currentRoute,
        modifier = Modifier
    )
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
fun PreviewStoreAppBottomNavigationBar2() {
    // Fake NavController for preview
    val fakeNavController = rememberNavController()
    val currentRoute = "home" // Set to a route from your list, e.g. "home"

    // Sample navigation items
    val navigationItems = listOf(
        NavigationItem(
            title = "Home",
            icon = painterResource(R.drawable.icon_home_outlined),
            iconActive = painterResource(R.drawable.icon_home_filled),
            route = "home"
        ),
        NavigationItem(
            title = "Product",
            icon = painterResource(R.drawable.icon_shopping_store_outlined),
            iconActive = painterResource(R.drawable.icon_shopping_store_filled),
            route = "product"
        ),
//        NavigationItem(
//            title = "Cart",
//            icon = painterResource(R.drawable.icon_cart_outlined),
//            iconActive = painterResource(R.drawable.icon_cart_filled),
//            route = "cart"
//        ),
        NavigationItem(
            title = "Favorite",
            icon = painterResource(R.drawable.icon_favourite_outlined),
            iconActive = painterResource(R.drawable.icon_favourite_filled),
            route = "favorite"
        ),
        NavigationItem(
            title = "Orders",
            icon = painterResource(R.drawable.icon_orders_outlined),
            iconActive = painterResource(R.drawable.icon_orders_filled),
            route = "orders"
        ),

        NavigationItem(
            title = "Profile",
            icon = painterResource(R.drawable.icon_profile_outlined),
            iconActive = painterResource(R.drawable.icon_profile_filled),
            route = "profile"
        )
    )

    StoreAppBottomNavigationBar(
        navigationItemList = navigationItems,
        navController = fakeNavController,
        currentRoute = currentRoute,
        modifier = Modifier
    )
}

