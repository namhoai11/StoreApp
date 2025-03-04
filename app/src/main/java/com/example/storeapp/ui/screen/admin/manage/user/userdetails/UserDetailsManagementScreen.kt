package com.example.storeapp.ui.screen.admin.manage.user.userdetails

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.storeapp.R
import com.example.storeapp.data.local.DataDummy
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.admin.AdminTopAppBar
import com.example.storeapp.ui.component.function.formatCurrency2
import com.example.storeapp.ui.component.function.timestampToDateOnlyString
import com.example.storeapp.ui.component.user.ConfirmDialog
import com.example.storeapp.ui.component.user.OrderDetailsRow
import com.example.storeapp.ui.component.user.UserRole
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.theme.StoreAppTheme

object UserDetailsManagementDestination : NavigationDestination {
    override val route = "userdetailsmanagement?userId={userId}"
    override val titleRes = R.string.usermanage_title
    fun createRoute(userId: String?): String {
        return "userdetailsmanagement?userId=$userId"
    }
}

@Composable
fun UserDetailsManagementScreen(
    navController: NavController,
    viewModel: UserDetailsManagementViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            AdminTopAppBar(
                R.drawable.arrowback,
                "Quản lý",
                "Tài khoản",
                { navController.navigateUp() },
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 16.dp)
            )
        },
//        bottomBar = {
//            AdminBottomNavigationBar(
//                navController = navController,
//                currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
//
//            )
//        }
    ) { innerPadding ->
        UserDetailsManagementContent(
            innerPadding = innerPadding,
            uiState = uiState,
            buttonUserClicked = { viewModel.onChooseSetRoleUser() }
        )

        if (uiState.isShowSetRoleDialog) {
            ConfirmDialog(
                onDismiss = { viewModel.dismissChooseSetRoleUser() },
                title = "Xác nhận",
                message = uiState.currentButtonText,
                confirmRemove = {
                    viewModel.confirmSetUserRoleClicked()
                }
            )
        }
    }
}

@Composable
fun UserDetailsManagementContent(
//    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    uiState: UserDetailsManagementUiState,
    buttonUserClicked: () -> Unit = {},
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        Column(
            modifier = Modifier
                .weight(1f) // Dùng weight để phần nội dung có thể cuộn được mà không che button
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_profile_filled),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(16.dp)) // Khoảng cách giữa ảnh và nội dung bên phải

                UserRole(modifier = Modifier.weight(1f), role = uiState.user.role)

            }


            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(16.dp)
            )
            OrderDetailsRow(
                imageId = R.drawable.icon_order,
                orderText = "Id",
                content = uiState.user.id
            )
            OrderDetailsRow(
                imageId = R.drawable.icon_user_filled,
                orderText = "Khách hàng:",
                content = (uiState.user.lastName) + " " + (uiState.user.firstName),
            )
            OrderDetailsRow(
                imageId = R.drawable.icon_email,
                orderText = "Email:",
                content = uiState.user.email,
            )
            OrderDetailsRow(
                imageId = R.drawable.icon_telephone,
                orderText = "Điện thoại:",
                content = uiState.user.phone,
            )

            OrderDetailsRow(
                imageId = R.drawable.icon_genders,
                orderText = "Giới tính:",
                content = uiState.user.gender.toString(),
            )
            OrderDetailsRow(
                imageId = R.drawable.icon_dateofbirth,
                orderText = "Ngày sinh:",
                content = timestampToDateOnlyString(uiState.user.dateOfBirth)
            )
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(16.dp)
            )

            OrderDetailsRow(
                imageId = R.drawable.icon_calendar,
                orderText = "Ngày tạo:",
                content = timestampToDateOnlyString(uiState.user.createdAt)
            )
            OrderDetailsRow(
                imageId = R.drawable.icon_money,
                orderText = "Số tiền đã tiêu:",
                content = formatCurrency2(uiState.userSpend),
            )
//            UserInfoRow(
//                icon = R.drawable.icon_money,
//                label = "Số tiền đã tiêu",
//                value = formatCurrency2(uiState.userSpend)
//            )
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column(
                    modifier = Modifier.padding(
                        top = 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
                ) {
//                    HorizontalDivider(
//                        thickness = 1.dp,
//                        color = Color.Black,
//                    )

                    Button(
                        enabled = (!uiState.isLoading),
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp)
                            .height(55.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        onClick = { buttonUserClicked() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = uiState.currentButtonText,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }

                }
            }
        }
    }
}

@Preview("Light Mode", showBackground = true)
@Preview("Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OrderScreenPreview() {
    StoreAppTheme {
        UserDetailsManagementContent(
//            modifier = Modifier,
            uiState = UserDetailsManagementUiState(
                user = DataDummy.user
            ),
            innerPadding = PaddingValues(0.dp)
        )
    }
}