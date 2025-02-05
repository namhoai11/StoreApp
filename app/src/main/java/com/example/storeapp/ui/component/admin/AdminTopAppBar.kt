package com.example.storeapp.ui.component.admin

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.storeapp.R

@Composable
fun AdminTopAppBar(
    @DrawableRes icon: Int,
    textRole: String,
    text: String,
    iconClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Cyan
        ),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Icon bên trái
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterStart) // Đặt Icon nằm ở bên trái
                    .size(24.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = false, color = Color.Gray) // Hiệu ứng Ripple
                    ) {
                        iconClicked()
                    },
                tint = Color.Black
            )

            // Column ở giữa
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.Center) // Đặt Column vào giữa
            ) {
                Text(
                    text = textRole,
                    color = Color.Black,
                    fontSize = 12.sp
                )
                Text(
                    text = text,
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(
                    thickness = 2.dp,
                    color = Color.Black,
                    modifier = Modifier.width(64.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDetailsTopAppBar(
    text: String,
    navController: NavController
) {
    TopAppBar(
        title = {
            Text(
                text = text,
//                        fontFamily = poppinsFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "back",
                modifier = Modifier
                    .clickable { navController.navigateUp() }
                    .padding(horizontal = 16.dp)
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewHomeTopAppBar() {
    AdminTopAppBar(R.drawable.icon_navhome, "Admin", "LaHoaiNam", {})
}


@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewManageTopAppBar() {
//    val navController = rememberNavController()
    AdminTopAppBar(
        R.drawable.arrowback,
        "Quan ly",
        "Don Hang",
        {}
    )
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAdminDetailsTopAppBar() {
    val navController = rememberNavController()
    AdminDetailsTopAppBar(
        "Chi tiết sản phẩm",
        navController
    )
}