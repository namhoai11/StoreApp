package com.example.storeapp.ui.component.user

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.storeapp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    navigateCartScreen: () -> Unit,
    navigateNotificateScreen: () -> Unit,
    cartQuantity: Int = 0,
    userName: String = ""
) {
    val itemCount by remember { mutableIntStateOf(1) }
    // Sử dụng scrollBehavior trong TopAppBar
    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Chào mừng",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                    Text(
                        text = userName,
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row {
                    BadgedBox(badge = {
                        if (itemCount > 0) {
                            Badge(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            ) {
                                Text("$itemCount")
                            }
                        }
                    }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_notification_outlined),
                            contentDescription = "",
                            modifier = Modifier.clickable {
                                navigateNotificateScreen()
                            }
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    BadgedBox(badge = {
                        if (itemCount > 0) {
                            Badge(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            ) {
                                Text("$cartQuantity")
                            }
                        }
                    }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_cart_outlined),
                            contentDescription = "",
                            modifier = Modifier.clickable {
                                navigateCartScreen()
                            }
                        )
                    }
                }
            }
        },
        scrollBehavior = scrollBehavior // Gắn scrollBehavior vào TopAppBar
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewHomeTopAppBar() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    HomeTopAppBar(scrollBehavior = scrollBehavior, {}, {})
}