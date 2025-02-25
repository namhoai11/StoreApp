package com.example.storeapp.data.local

import com.example.storeapp.R
import com.example.storeapp.ui.navigation.NavigationItem
import com.example.storeapp.ui.screen.admin.manage.category.CategoryManagementDestination
import com.example.storeapp.ui.screen.admin.manage.coupon.CouponManagementDestination
import com.example.storeapp.ui.screen.admin.manage.orders.OrderManagementDestination
import com.example.storeapp.ui.screen.admin.manage.product.ProductManagementDestination
import com.example.storeapp.ui.screen.favorite.FavoriteDestination
import com.example.storeapp.ui.screen.ourproduct.OurProductDestination

object ManagementNavigationItems {
    val navigationItemList = listOf(
        NavigationItem(
            title = OrderManagementDestination.titleRes,
            icon = R.drawable.admin_iconorders,
            iconActive = R.drawable.admin_iconorders,
            route = OrderManagementDestination.route
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
            title = CategoryManagementDestination.titleRes,
            icon = R.drawable.admin_iconcategory,
            iconActive = R.drawable.admin_iconcategory,
            route = CategoryManagementDestination.route
        ),
        NavigationItem(
            title = ProductManagementDestination.titleRes,
            icon = R.drawable.admin_iconproduct,
            iconActive = R.drawable.admin_iconproduct,
            route = ProductManagementDestination.route
        ),
        NavigationItem(
            title = R.string.couponmanage_title,
            icon = R.drawable.admin_iconcoupon,
            iconActive = R.drawable.admin_iconcoupon,
            route = CouponManagementDestination.route
        )
    )
}