package com.example.storeapp.data.local

import com.example.storeapp.R
import com.example.storeapp.ui.navigation.NavigationItem
import com.example.storeapp.ui.screen.admin.dashboard.DashboardAdminDestination
import com.example.storeapp.ui.screen.admin.manage.ManegeAdminDestination

object AdminBottomNavigationItemsProvider {
    val navigationItemList = listOf(
        NavigationItem(
            title = DashboardAdminDestination.titleRes,
            icon = R.drawable.admin_icondashboard,
            iconActive = R.drawable.admin_icondashboard,
            route = DashboardAdminDestination.route
        ),
        NavigationItem(
            title = ManegeAdminDestination.titleRes,
            icon = R.drawable.admin_iconmanage,
            iconActive = R.drawable.admin_iconmanage,
            route = ManegeAdminDestination.route
        ),
    )
}