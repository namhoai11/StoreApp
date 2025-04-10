﻿package com.example.storeapp.ui.screen.login.forgotpassword

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.storeapp.R
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.user.SignInText
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.screen.login.signup.LoginTextField
import com.example.storeapp.ui.theme.StoreAppTheme


object ForgotPasswordDestination : NavigationDestination {
    override val route = "forgot_password"
    override val titleRes = R.string.forgot_password
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    onNavigateSignIn: () -> Unit,
//    onNavigateVerify: () -> Unit,
    viewModel: ForgotPasswordViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(horizontal = 16.dp),
                title = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = stringResource(id = R.string.forgot_password),
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
        },
    ) { innerPadding ->
        ForgotPasswordContent(
            uiState = uiState, innerPadding = innerPadding,
            onEmailChange = { viewModel.onEmailChange(it) },
            sendPasswordResetEmail = { viewModel.sendPasswordResetEmail { onNavigateSignIn() } },
            onNavigateSignIn = onNavigateSignIn
        )

    }
}

@Composable
fun ForgotPasswordContent(
    uiState: ForgotPasswordUiState,
    innerPadding: PaddingValues,
    onEmailChange: (String) -> Unit = {},
    sendPasswordResetEmail: () -> Unit = {},
    onNavigateSignIn: () -> Unit = {},
//    onNavigateVerify: () -> Unit = {},
) {

    Column(
        Modifier
            .padding(innerPadding)
            .fillMaxHeight()
            .fillMaxWidth()
            .background(color = Color(android.graphics.Color.parseColor("#ffffff"))),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Đừng lo lắng. Chỉ cần nhập email đăng ký để nhận liên kết đặt lại mật khẩu nhé.",
            Modifier.padding(vertical = 8.dp, horizontal = 48.dp),
            fontSize = 14.sp,
            textAlign = TextAlign.Center, // Căn giữa văn bản theo chiều ngang
            color = Color(0xFF7D32A8),
        )
        Image(
            painter = painterResource(id = R.drawable.sticker_forgot),
            contentDescription = null,
            modifier = Modifier.height(320.dp)
        )
        LoginTextField(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            value = uiState.email,
            onValueChange = { onEmailChange(it) },
            placeholder = "Email",
            isPasswordField = false,
            leadingIcon = R.drawable.icon_email
        )
        Button(
            onClick = { sendPasswordResetEmail() },
            Modifier
                .fillMaxWidth()
                .height(66.dp)
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7D32A8)
            ),
            shape = RoundedCornerShape(12)
        ) {
            Text(
                text = "Gửi",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        SignInText(
            normalText = R.string.remembered_password,
            clickableText = R.string.login_title,
//                textAnnomation = R.string.signin_annotation,
            onTextClicked = {
                onNavigateSignIn()
            },
            modifier = Modifier.navigationBarsPadding()
        )

    }
}


@Preview("lightTheme", showBackground = true)
@Preview("DarkTheme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewForgotPassword() {
    StoreAppTheme {
        ForgotPasswordContent(
            uiState = ForgotPasswordUiState(),
            innerPadding = PaddingValues(0.dp)
        )
    }
}