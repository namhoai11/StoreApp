package com.example.storeapp.ui.screen.notification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.storeapp.R
import com.example.storeapp.model.NotificationModel
import com.example.storeapp.ui.component.user.NotificationItem
import com.example.storeapp.ui.navigation.NavigationDestination

object NotificateDestination : NavigationDestination {
    override val route = "notificate"
    override val titleRes = R.string.notificate_title
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    navController: NavController
) {
    val notificateItems: ArrayList<NotificationModel> = arrayListOf(
        NotificationModel(
            id = 1,
            notificationType = "Shopping",
            firstProductImage = "",
            quantityCheckout = 2,
            firstProductName = "Men's Casual Slim Fit Shirt",
            message = "Your order has been shipped",
            date = "20 Agustus 2023",
            messageDetail = "Your order has been shipped",
            isRead = false,
        ),
        NotificationModel(
            id = 2,
            notificationType = "Info",
            date = "20 Agustus 2023",
            message = "Welcome to Vibe Store 🙌",
            messageDetail = "We’re thrilled to have you on board! Explore amazing deals and start shopping now.",
            isRead = false,
        )
    )
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(horizontal = 16.dp),
                title = {
                    Text(
                        text = "Notification",
//                        fontFamily = poppinsFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back",
                        modifier = Modifier.clickable { navController.navigateUp() }
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            LazyColumn {
                items(notificateItems) { notif ->
                    NotificationItem(
                        notificationType = notif.notificationType,
                        date = notif.date,
                        message = notif.message,
                        firstProductImage = notif.firstProductImage,
                        firstProductName = notif.firstProductName,
                        quantityCheckout = notif.quantityCheckout,
                        messageDetail = notif.messageDetail,
                        isRead = notif.isRead,
                        onNotificationClick = {
                        }
                    )
                }
            }
        }

    }

}