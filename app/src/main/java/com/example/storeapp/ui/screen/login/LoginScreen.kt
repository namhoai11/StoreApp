package com.example.storeapp.ui.screen.login


import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.storeapp.R
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.user.SignInText
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.screen.login.signup.LoginTextField
import com.example.storeapp.ui.screen.login.signup.SignUpViewModel
import com.example.storeapp.ui.theme.StoreAppTheme


object LoginDestination : NavigationDestination {
    override val route = "login"
    override val titleRes = R.string.login_title
}

@Composable
fun LoginScreen(
    onNavigateSignUp: () -> Unit,
    onNavigateForgotPassword: () -> Unit,
    onNavigateHome: () -> Unit,
    viewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val uiState by viewModel.uiState.collectAsState()  // Lấy trạng thái từ ViewModel
    Column(
        Modifier
            .fillMaxHeight()
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
                text = "Đăng nhập",
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

        LoginTextField(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            value = uiState.email,
            onValueChange = { viewModel.onEmailChange(it) },
            placeholder = "Email",
            isPasswordField = false,
            leadingIcon = R.drawable.icon_email
        )
        LoginTextField(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            placeholder = "Mật khẩu",
            isPasswordField = true,
            leadingIcon = R.drawable.icon_padlock_filled
        )
        val interactionSource = remember { MutableInteractionSource() }
        Text(
            text = stringResource(id = R.string.forgot_password),
            modifier = Modifier
                .clickable(interactionSource = interactionSource, indication = null) {
                    onNavigateForgotPassword()
                }
                .padding(top = 16.dp, bottom = 16.dp, end = 16.dp)
                .align(Alignment.End),
            style = TextStyle(
                color = Color(0xFF7D32A8),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        )
        Button(
            onClick = { viewModel.SignIn(onNavigateHome) },
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
                text = stringResource(id = R.string.login_title),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

        }
        SignInText(
            normalText = R.string.non_account,
            clickableText = R.string.signup_title,
            textAnnomation = R.string.signup_annotation,
            onTextClicked = {
                onNavigateSignUp()
            }
        )
        Row() {
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = null,
                Modifier.padding(8.dp)
            )
//            Image(
//                painter = painterResource(id = R.drawable.twitter),
//                contentDescription = null,
//                Modifier.padding(8.dp)
//            )
            Image(
                painter = painterResource(id = R.drawable.facebook),
                contentDescription = null,
                Modifier.padding(8.dp)
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

@Preview("LighTheme", showBackground = true)
@Preview("DarkTheme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewLoginScreen() {
    StoreAppTheme {
        LoginScreen(onNavigateSignUp = {},
            onNavigateForgotPassword = {},{})
    }
}
