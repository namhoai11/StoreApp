package com.example.storeapp.ui.screen.login.verify

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.storeapp.R
import com.example.storeapp.ui.component.user.SignInText
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.theme.StoreAppTheme

object VerifyAccountDestination : NavigationDestination {
    override val route = "verify_account"
    override val titleRes = R.string.verify_account
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyScreen(
    navController: NavController,
    onNavigateDoneVerify: () -> Unit
) {
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
                            text = stringResource(id = R.string.verify_account),
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
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxHeight()
                .fillMaxWidth()
                .background(color = Color(android.graphics.Color.parseColor("#ffffff"))),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Một mã xác nhận sẽ được gửi tới email của bạn. Một bước nữa thôi.",
                Modifier.padding(vertical = 8.dp, horizontal = 48.dp),
                fontSize = 14.sp,
                textAlign = TextAlign.Center, // Căn giữa văn bản theo chiều ngang
                color = Color(0xFF7D32A8),
            )
            Image(
                painter = painterResource(id = R.drawable.sticker_comfirmaccount),
                contentDescription = null,
                modifier = Modifier.height(320.dp)
            )
//            var user by remember { mutableStateOf("") }
//            LoginTextField(
//                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
//                value = user,
//                onValueChange = { user = it },
//                placeholder = "Email",
//                isPasswordField = false,
//                leadingIcon = R.drawable.icon_email
//            )
//            var otpValues by remember { mutableStateOf(List(4) { "" }) }
//            var otpValues by remember { mutableStateOf(listOf("", "", "", "")) }
//            val focusManager = LocalFocusManager.current
            var otpValues = remember { mutableStateListOf("", "", "", "") }
            val focusRequesters = List(otpValues.size) { FocusRequester() }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                otpValues.forEachIndexed { index, value ->
                    TextField(
                        value = value,
                        onValueChange = { newValue ->
                            if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                                otpValues[index] = newValue // Cập nhật trực tiếp vào danh sách
                            }
                            // Chuyển focus khi người dùng nhập đủ số
                            if (newValue.isNotEmpty() && index < otpValues.size - 1) {
                                focusRequesters[index + 1].requestFocus()
                            } else if (newValue.isEmpty() && index > 0) {
                                // Quay lại trường trước nếu người dùng xóa
                                focusRequesters[index - 1].requestFocus()
                            }
                        },
                        modifier = Modifier
                            .size(80.dp)
                            .padding(4.dp)
                            .border(
                                1.dp, Color(0xFF7D32A8),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .focusRequester(focusRequesters[index]), // Gán focusRequester cho TextField

                        textStyle = TextStyle(fontSize = 24.sp, textAlign = TextAlign.Center),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                    )
                }
            }
            Button(
                onClick = { onNavigateDoneVerify() },
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
                normalText = R.string.dont_received_code,
                clickableText = R.string.resend,
//                textAnnomation = R.string.resend_annotation,
                onTextClicked = {
                },
                modifier = Modifier.navigationBarsPadding(),
            )
        }
    }
}

@Preview("lightTheme", showBackground = true)
@Preview("DarkTheme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewVerifyPassword() {
    StoreAppTheme {
        VerifyScreen(navController = rememberNavController(),
            {})
    }
}