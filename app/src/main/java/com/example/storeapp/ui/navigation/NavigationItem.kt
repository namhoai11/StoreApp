package com.example.storeapp.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class NavigationItem(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    @DrawableRes val iconActive: Int,
    val route: String
)

