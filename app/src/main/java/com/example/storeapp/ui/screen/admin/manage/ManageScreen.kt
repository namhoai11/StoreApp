package com.example.storeapp.ui.screen.admin.manage

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.storeapp.R
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
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Menu quản lý",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
            Column(
                modifier = Modifier.padding(16.dp,8.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ManagementItem(R.drawable.admin_iconorders, "Đơn hàng")
                    ManagementItem(R.drawable.admin_iconcustomer, "Khách Hàng")
                    ManagementItem(R.drawable.admin_iconmanager, "Tài khoản Admin")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ManagementItem(R.drawable.admin_iconcategory, "Danh mục")
                    ManagementItem(R.drawable.admin_iconproduct, "Sản phẩm")
                    ManagementItem(R.drawable.admin_iconcoupon, "Khuyến mãi")
                }
            }
        }
    }
}

@Composable
fun ManagementItem(
    @DrawableRes managementIcon: Int,
    managementText: String,
) {
    Box(modifier = Modifier.size(110.dp)) {
        Card(
            modifier = Modifier
                .aspectRatio(1f), // Đảm bảo Card có tỉ lệ vuông
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
                    contentDescription = managementText,
                    modifier = Modifier.size(32.dp)
                )

                Text(
                    text = managementText,
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
        ManagementItem(R.drawable.admin_iconorders, "Don hang")
    }
}