package com.example.storeapp.ui.screen.profile.editprofile

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.storeapp.R
import com.example.storeapp.model.Gender
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.admin.FilterOption
import com.example.storeapp.ui.component.function.timestampToDateOnlyString
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.screen.admin.manage.coupon.add_coupon.AddDateField
import com.example.storeapp.ui.screen.login.signup.LoginTextField
import com.example.storeapp.ui.theme.StoreAppTheme

object ProfileEditDestination : NavigationDestination {
    override val route = "profileedit"
    override val titleRes = R.string.profiledetail_title
}

@Composable
fun ProfileEditScreen(
    navController: NavController,
    viewModel: ProfileEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    ProfileEditContent(uiState = uiState,
        onLastNameChange = { viewModel.onLastNameChange(it) },
        onFirstNameChange = { viewModel.onFirstNameChange(it) },
        onDateOfBirthChange = { viewModel.onDateOfBirthChange(it) },
        onGenderSelected = { viewModel.onGenderSelected(it) },
        onPhoneChange = { viewModel.onPhoneChange(it) },
        onConfirmClick = { viewModel.updateUserInfo { navController.navigateUp() } },
        onCancelClick = { navController.navigateUp() }
    )


}


@Composable
fun ProfileEditContent(
    uiState: ProfileEditUiState,
    onLastNameChange: (String) -> Unit = {},
    onFirstNameChange: (String) -> Unit = {},
    onDateOfBirthChange: (String) -> Unit = {},
    onGenderSelected: (Gender) -> Unit = {},
    onPhoneChange: (String) -> Unit = {},
    onConfirmClick: () -> Unit = {},
    onCancelClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        Modifier
            .padding(WindowInsets.statusBars.asPaddingValues())
            .fillMaxHeight()
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .background(color = Color(android.graphics.Color.parseColor("#ffffff"))),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Sửa thông tin",
                fontSize = 30.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                color = Color(android.graphics.Color.parseColor("#7d32a8"))
            )
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(
                thickness = 3.dp,
                color = Color(android.graphics.Color.parseColor("#7d32a8")),
                modifier = Modifier.width(64.dp),

                )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LoginTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, bottom = 8.dp, top = 8.dp),
                value = uiState.lastName,
                onValueChange = { onLastNameChange(it) },
                placeholder = "Tên",
                isPasswordField = false,
                leadingIcon = R.drawable.icon_user_outlined
            )
            LoginTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp, bottom = 8.dp, top = 8.dp),
                value = uiState.firstName,
                onValueChange = { onFirstNameChange(it) },
                placeholder = "Họ",
                isPasswordField = false,
                leadingIcon = R.drawable.icon_user_outlined
            )
        }
        AddDateField(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            title = "Ngày sinh",
            isEditing = true,
            dateInput = timestampToDateOnlyString(uiState.dateOfBirth),
            onDateChange = { onDateOfBirthChange(it) }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circle at the bottom
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .background(Color.Gray, shape = CircleShape)
//                        .align(Alignment.BottomStart) // Đặt chấm tròn ở dưới cùng
            )
            Text(
                text = "Giới tính",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            val listOptions = Gender.entries.map { it.toString() }
            FilterOption(
                modifier = Modifier.width(250.dp),
                isEditing = true,
                listOptions = listOptions,
                selectedOption = uiState.gender.toString(),
                onOptionSelected = { onGenderSelected(Gender.fromString(it)!!) }
            )
        }
//        LoginTextField(
//            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
//            value = uiState.email,
//            onValueChange = { onEmailChange(it) },
//            placeholder = "Email",
//            isPasswordField = false,
//            leadingIcon = R.drawable.icon_email
//        )
        LoginTextField(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            value = uiState.phone,
            onValueChange = { onPhoneChange(it) },
            placeholder = "Số điện thoại",
            isPasswordField = false,
            leadingIcon = R.drawable.icon_telephone
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                onConfirmClick()
            },
            Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(
                    horizontal = 32.dp,
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7D32A8)
            ),
            shape = RoundedCornerShape(12)
        ) {
            Text(
                text = "Xác nhận",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = { onCancelClick() },
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
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview("LightMode", showBackground = true)
@Preview("DarkMode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSignUpScreen() {
    StoreAppTheme {
        ProfileEditContent(
            uiState = ProfileEditUiState(

            )
        )
    }
}