package com.example.storeapp.ui.screen.profile

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.storeapp.R
import com.example.storeapp.data.local.SettingProfileNavigatonItemProvider
import com.example.storeapp.ui.component.StoreAppBottomNavigationBar
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.theme.StoreAppTheme

object ProfileDestination : NavigationDestination {
    override val route = "profile"
    override val titleRes = R.string.profile_title
}

@Composable
fun ProfileScreen(
    navController: NavController,
    ) {
    Scaffold(
        bottomBar = {
            StoreAppBottomNavigationBar(
                navController = navController,
                currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier.padding(contentPadding)
        ) {
            ProfileTopBar({})
            ProfileScreenContent()
        }
    }
}

@Composable
fun ProfileTopBar(
    navigateEditAccountScreen: () -> Unit,
) {
    // Sử dụng scrollBehavior trong TopAppBar
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, start = 8.dp, end = 16.dp, bottom = 16.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.icon_profile_filled),
            contentDescription = "profile",
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(80.dp))
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ),
            tint = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
//                    Text(
//                        text = "Welcome Back",
//                        color = Color.Black,
//                        fontSize = 14.sp
//                    )
            Text(
                text = "LaHoaiNam",
                color = Color.Black,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row {
//                    BadgedBox(badge = {
//                        if (itemCount > 0) {
//                            Badge(
//                                containerColor = Color.Red,
//                                contentColor = Color.White
//                            ) {
//                                Text("$itemCount")
//                            }
//                        }
//                    }
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.icon_notification_outlined),
//                            contentDescription = "",
//                            modifier = Modifier.clickable {
//                                navigateNotificateScreen()
//                            }
//                        )
//                    }
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = painterResource(id = R.drawable.edit),
                contentDescription = "",
                modifier = Modifier.clickable {
                    navigateEditAccountScreen()
                }
            )
        }
    }

}

@Composable
fun ProfileScreenContent() {
    Card(
        shape = RoundedCornerShape(
            topStart = 32.dp,
            topEnd = 32.dp,
            bottomStart = 0.dp,
            bottomEnd = 0.dp
        ),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            item {
                Text(
                    text = "Account Settings",
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 32.dp),
                )
            }
            items(SettingProfileNavigatonItemProvider.navigationItemList) { item ->
                SettingItem(icon = item.icon, title = item.title, descript = item.description) {
                }
            }
            item {
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier
                        .padding(
                            horizontal = 32.dp,
                            vertical = 16.dp
                        )
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Đăng xuất",
//                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


@Composable
fun SettingItem(
//    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    title: String,
    descript: String,
    onItemClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onItemClicked)
            .padding(start = 32.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = title,
            modifier = Modifier.size(40.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
            Text(
                text = descript,
                fontSize = 12.sp
            )

        }
    }

}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewProfileScreenConten() {
    StoreAppTheme {
        ProfileScreenContent()

    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSettingItem() {
    SettingItem(
        icon = R.drawable.icon_pinlocation,
        title = "Địa chỉ",
        descript = "Cập nhật địa chỉ nhận hàng"
    ) {

    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewHomeTopAppBar() {
    ProfileTopBar({})
}