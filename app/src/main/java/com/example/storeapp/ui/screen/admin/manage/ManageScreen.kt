package com.example.storeapp.ui.screen.admin.manage

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.storeapp.R
import com.example.storeapp.data.local.ManagementNavigationItems
import com.example.storeapp.ui.component.admin.AdminBottomNavigationBar
import com.example.storeapp.ui.component.admin.AdminTopAppBar
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.theme.StoreAppTheme

object ManegeAdminDestination : NavigationDestination {
    override val route = "manage"
    override val titleRes = R.string.manage_title
}

@Composable
fun ManageScreen(
    navController: NavController,
    navigateUserApp: () -> Unit
) {
    Scaffold(
        topBar = {
            AdminTopAppBar(
                R.drawable.icon_navhome,
                "Admin",
                "LaHoaiNam",
                navigateUserApp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 48.dp)
            )
        },
        bottomBar = {
            AdminBottomNavigationBar(
                navController = navController,
                currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            contentPadding = innerPadding,
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(ManagementNavigationItems.navigationItemList) { item ->
                ManagementItem(managementIcon = item.icon, managementText = item.title,
                    onManagementItemClicked = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    })
            }
        }
    }
}


@Composable
fun ManagementItem(
    @DrawableRes managementIcon: Int,
    @StringRes managementText: Int,
    onManagementItemClicked: () -> Unit,
) {
    Box(modifier = Modifier.size(110.dp)) {
        Card(
            modifier = Modifier
                .aspectRatio(1f) // Đảm bảo Card có tỉ lệ vuông
                .clickable(onClick = onManagementItemClicked),

            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center, // Căn giữa các phần tử theo chiều dọc
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)// Đảm bảo Column chiếm toàn bộ không gian của Card
            ) {
                Icon(
                    painter = painterResource(id = managementIcon),
                    contentDescription = stringResource(id = managementText),
                    modifier = Modifier.size(32.dp)
                )

                Text(
                    text = stringResource(id = managementText),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 14.sp,
                    textAlign = TextAlign.Center, // Căn giữa văn bản theo chiều ngang
                    modifier = Modifier.fillMaxWidth() // Đảm bảo Text chiếm toàn bộ chiều rộng để căn giữa
                )
            }
        }
    }
}

@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewManageScreen() {
    StoreAppTheme {
        ManageScreen(
            navController = rememberNavController(),
            {}
        )
    }
}

@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewManagementItem() {
    StoreAppTheme {
        ManagementItem(R.drawable.admin_iconorders, R.string.orders_title, {})
    }
}