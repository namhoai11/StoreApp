package com.example.storeapp.data.local

import com.example.storeapp.R
import com.example.storeapp.ui.navigation.NavigationItem
import com.example.storeapp.ui.screen.favorite.FavoriteDestination
import com.example.storeapp.ui.screen.home.HomeDestination
import com.example.storeapp.ui.screen.order.OrdersDestination
import com.example.storeapp.ui.screen.ourproduct.OurProductDestination
import com.example.storeapp.ui.screen.profile.ProfileDestination

object ManagementNavigationItems {
    val navigationItemList = listOf(
        NavigationItem(
            title = HomeDestination.titleRes,
            icon = R.drawable.admin_iconorders,
            iconActive = R.drawable.admin_iconorders,
            route = HomeDestination.route
        ),
        NavigationItem(
            title = OurProductDestination.titleRes,
            icon = R.drawable.admin_iconcustomer,
            iconActive = R.drawable.admin_iconcustomer,
            route = OurProductDestination.route
        ),
        NavigationItem(
            title = FavoriteDestination.titleRes,
            icon = R.drawable.admin_iconmanager,
            iconActive = R.drawable.admin_iconmanager,
            route = FavoriteDestination.route
        ),
        NavigationItem(
            title = OrdersDestination.titleRes,
            icon = R.drawable.admin_iconcategory,
            iconActive = R.drawable.admin_iconcategory,
            route = OrdersDestination.route
        ),
        NavigationItem(
            title = ProfileDestination.titleRes,
            icon = R.drawable.admin_iconproduct,
            iconActive = R.drawable.admin_iconproduct,
            route = ProfileDestination.route
        ),
        NavigationItem(
            title = R.string.profile_title,
            icon = R.drawable.admin_iconcoupon,
            iconActive = R.drawable.admin_iconcoupon,
            route = ProfileDestination.route
        )
    )
}