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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.storeapp.data.local.BottomNavigationItemsProvider


@Composable
fun StoreAppBottomNavigationBar(
    navController: NavController,
    currentRoute: String?,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        BottomNavigationItemsProvider.navigationItemList.forEach { navItem ->
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
                            painter = painterResource(id = if (currentRoute == navItem.route) navItem.iconActive else navItem.icon) ,
                            contentDescription = stringResource(id = navItem.title) ,
                            tint = if (currentRoute == navItem.route) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp)) // Khoảng cách giữa icon và title
                        Text(
                            text = stringResource(id = navItem.title) ,
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

    StoreAppBottomNavigationBar(
        navController = rememberNavController(), // Không ảnh hưởng trong Preview
        currentRoute = "home", // Thiết lập một route cố định để kiểm tra Preview
        modifier = Modifier
    )
}


