package com.example.storeapp.data.local

import com.example.storeapp.R
import com.example.storeapp.ui.screen.favorite.FavoriteDestination
import com.example.storeapp.ui.navigation.NavigationItem
import com.example.storeapp.ui.screen.ourproduct.OurProductDestination
import com.example.storeapp.ui.screen.home.HomeDestination
import com.example.storeapp.ui.screen.order.OrdersDestination
import com.example.storeapp.ui.screen.profile.ProfileDestination


object BottomNavigationItemsProvider {
    val navigationItemList = listOf(
        NavigationItem(
            title = HomeDestination.titleRes,
            icon = R.drawable.icon_home_outlined,
            iconActive = R.drawable.icon_home_filled,
            route = HomeDestination.route
        ),
        NavigationItem(
            title = OurProductDestination.titleRes,
            icon = R.drawable.icon_shopping_store_outlined,
            iconActive = R.drawable.icon_shopping_store_filled,
            route = OurProductDestination.route
        ),
        NavigationItem(
            title = FavoriteDestination.titleRes,
            icon = R.drawable.icon_favourite_outlined,
            iconActive = R.drawable.icon_favourite_filled,
            route = FavoriteDestination.route
        ),
        NavigationItem(
            title = OrdersDestination.titleRes,
            icon = R.drawable.icon_orders_outlined,
            iconActive = R.drawable.icon_orders_filled,
            route = OrdersDestination.route
        ),
        NavigationItem(
            title = ProfileDestination.titleRes,
            icon = R.drawable.icon_profile_outlined,
            iconActive = R.drawable.icon_profile_filled,
            route = ProfileDestination.route
        )
    )
}
