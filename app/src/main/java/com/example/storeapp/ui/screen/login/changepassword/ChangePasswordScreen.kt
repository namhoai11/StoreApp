package com.example.storeapp.ui.screen.login.changepassword

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.storeapp.R
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.screen.login.LoginDestination
import com.example.storeapp.ui.screen.login.signup.LoginTextField
import com.example.storeapp.ui.theme.StoreAppTheme

object ChangePasswordDestination : NavigationDestination {
    override val route = "change_password"
    override val titleRes = R.string.change_password
}

@Composable
fun ChangePasswordScreen(
    navController: NavController,
    viewModel: ChangePasswordViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    ChangePasswordContent(uiState = uiState,
        onOldPasswordChange = { viewModel.onOldPasswordChange(it) },
        onNewPasswordChange = { viewModel.onNewPasswordChange(it) },
        onConfirmNewPasswordChange = { viewModel.onConfirmNewPasswordChange(it) },
        onConFirm = {
            viewModel.changePassword {
                navController.navigate(LoginDestination.route)
            }
        }
    )
}

@Composable
fun ChangePasswordContent(
    uiState: ChangePasswordUiState,
    onOldPasswordChange: (String) -> Unit = {},
    onNewPasswordChange: (String) -> Unit = {},
    onConfirmNewPasswordChange: (String) -> Unit = {},
    onConFirm: () -> Unit = {}
) {
    Column(
        Modifier
            .fillMaxHeight()
            .padding(WindowInsets.statusBars.asPaddingValues())
            .fillMaxWidth()
            .background(color = Color(android.graphics.Color.parseColor("#ffffff"))),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.top_background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxWidth()
        )
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.height(150.dp)
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = stringResource(id = R.string.change_password),
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
            value = uiState.oldPassword,
            onValueChange = { onOldPasswordChange(it) },
            placeholder = "Mật khẩu cũ",
            isPasswordField = true,
            leadingIcon = R.drawable.icon_padlock_filled
        )
        LoginTextField(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            value = uiState.newPassword,
            onValueChange = { onNewPasswordChange(it) },
            placeholder = "Mật khẩu mới",
            isPasswordField = true,
            leadingIcon = R.drawable.icon_padlock_filled
        )
        LoginTextField(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            value = uiState.confirmNewPassword,
            onValueChange = { onConfirmNewPasswordChange(it) },
            placeholder = "Nhập lại mật khẩu mới",
            isPasswordField = true,
            leadingIcon = R.drawable.icon_padlock_filled
        )
        Button(
            onClick = onConFirm,
            Modifier
                .fillMaxWidth()
                .height(66.dp)
                .padding(start = 64.dp, end = 64.dp, top = 8.dp, bottom = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7D32A8)
            ),
            shape = RoundedCornerShape(12)
        ) {
            Text(
                text = stringResource(id = R.string.change_password),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

        }
        Image(
            painter = painterResource(id = R.drawable.bottom_background),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview("LightMode", showBackground = true)
@Preview("DarkMode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewChangePasswordContent() {
    StoreAppTheme {
        ChangePasswordContent(
            uiState = ChangePasswordUiState()
        )
    }
}