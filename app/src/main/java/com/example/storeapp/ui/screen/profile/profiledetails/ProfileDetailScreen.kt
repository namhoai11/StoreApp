package com.example.storeapp.ui.screen.profile.profiledetails

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.storeapp.R
import com.example.storeapp.model.UserModel
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.function.formatCurrency2
import com.example.storeapp.ui.component.function.timestampToDateOnlyString
import com.example.storeapp.ui.navigation.NavigationDestination


object ProfileDetailDestination : NavigationDestination {
    override val route = "profiledetail"
    override val titleRes = R.string.profiledetail_title
}

@Composable
fun ProfileDetailScreen(
    navController: NavController,
    onNavigateEditProfile: () -> Unit,
    viewModel: ProfileDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentUser by viewModel.user.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }
    currentUser?.let {
        ProfileDetailContent(
            uiState = uiState, currentUser = it,
            onEditProfileClick = onNavigateEditProfile,
            onCloseClick = { navController.navigateUp() }
        )
    }
}


@Composable
fun ProfileDetailContent(
    uiState: ProfileDetailUiState = ProfileDetailUiState(),
    currentUser: UserModel = UserModel(),
    onEditProfileClick: () -> Unit = {},
    onCloseClick: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 64.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "Thông tin tài khoản".uppercase(),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier

//                .weight(1f)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(10.dp),
        ) {
            Row(
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_profile_filled),
                    contentDescription = "profile",
                    modifier = Modifier
                        .size(60.dp)
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
                    Text(
                        text = currentUser.firstName + " " + currentUser.lastName,
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
//                        modifier = Modifier.weight(0.4f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_user_filled),
                            contentDescription = currentUser.role.toString(),
                            modifier = Modifier.size(20.dp),
                            colorFilter = ColorFilter.tint(Color.Gray) // Làm biểu tượng đồng bộ màu xám
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = currentUser.role.toString(),
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
//                    Row(
////                        modifier = Modifier.fillMaxWidth(),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        UserInfoRow(icon = R.drawable.icon_user_filled, label = "khách hàng")
//                    }
                }
            }

        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Chi tiết",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(10.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                UserInfoRow(
                    icon = R.drawable.icon_email,
                    label = "Email",
                    value = currentUser.email
                )
                UserInfoRow(
                    icon = R.drawable.icon_telephone,
                    label = "Số điện thoại",
                    value = currentUser.phone
                )
                UserInfoRow(icon = R.drawable.icon_genders, label = "Giới tính", value = "Nam")
                UserInfoRow(
                    icon = R.drawable.icon_dateofbirth,
                    label = "Ngày sinh",
                    value = timestampToDateOnlyString(currentUser.dateOfBirth)
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    modifier = Modifier.padding(16.dp)
                )
                UserInfoRow(
                    icon = R.drawable.icon_money,
                    label = "Số tiền đã tiêu",
                    value = formatCurrency2(uiState.userSpend)
                )

            }
        }

        Button(
            onClick = { onEditProfileClick() },
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
                text = "Chỉnh sửa",
//                        color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        OutlinedButton(
            onClick = { onCloseClick() },
            modifier = Modifier
                .padding(
                    horizontal = 32.dp,
                )
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "Đóng",
//                        color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun UserInfoRow(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    label: String = "",
    value: String = "",
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(0.4f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = label,
                modifier = Modifier.size(20.dp),
                colorFilter = ColorFilter.tint(Color.Gray) // Làm biểu tượng đồng bộ màu xám
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.weight(0.6f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewProfileDetailContent() {
    ProfileDetailContent()
}