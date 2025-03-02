package com.example.storeapp.ui.screen.login.signup

import android.content.res.Configuration
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.storeapp.R
import com.example.storeapp.model.Gender
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.admin.FilterOption
import com.example.storeapp.ui.component.function.timestampToDateOnlyString
import com.example.storeapp.ui.component.user.SignInText
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.screen.admin.manage.coupon.add_coupon.AddDateField
import com.example.storeapp.ui.theme.StoreAppTheme


object SignUpDestination : NavigationDestination {
    override val route = "signup"
    override val titleRes = R.string.signup_title
}

@Composable
fun SignUpScreen(
    onNavigateSignIn: () -> Unit,
    viewModel: SignUpViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollState = rememberScrollState()

    val uiState by viewModel.uiState.collectAsState()  // Lấy trạng thái từ ViewModel
    Column(
        Modifier
            .padding(WindowInsets.statusBars.asPaddingValues())
            .fillMaxHeight()
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .background(color = Color(android.graphics.Color.parseColor("#ffffff"))),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                text = "Đăng Ký",
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
                onValueChange = { viewModel.onLastNameChange(it) },
                placeholder = "Tên",
                isPasswordField = false,
                leadingIcon = R.drawable.icon_user_outlined
            )
            LoginTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp, bottom = 8.dp, top = 8.dp),
                value = uiState.firstName,
                onValueChange = { viewModel.onFirstNameChange(it) },
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
            onDateChange = { viewModel.onDateOfBirthChange(it) }
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
                onOptionSelected = { viewModel.onGenderSelected(Gender.fromString(it)!!) }
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
            value = uiState.phone,
            onValueChange = { viewModel.onPhoneChange(it) },
            placeholder = "Số điện thoại",
            isPasswordField = false,
            leadingIcon = R.drawable.icon_telephone
        )
        LoginTextField(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            placeholder = "Mật khẩu",
            isPasswordField = true,
            leadingIcon = R.drawable.icon_padlock_filled
        )
        LoginTextField(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            value = uiState.confirmPassword,
            onValueChange = { viewModel.onConfirmPasswordChange(it) },
            placeholder = "Nhập lại mật khẩu",
            isPasswordField = true,
            leadingIcon = R.drawable.icon_padlock_filled
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.padding(top = 16.dp)
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Checkbox(
                checked = uiState.isChecked,
                onCheckedChange = { viewModel.onCheckedChange(it) },
                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF7D32A8))
            )
            Text(
                text = "Tôi đồng ý với chính sách bảo mật và điều khoản",
                fontSize = 14.sp,
//                modifier = Modifier.padding(start = 8.dp)
            )
        }
        val context = LocalContext.current
        LaunchedEffect(uiState.successMessage) {
            Log.d("SignUpScreen", "successMessage: ${uiState.successMessage}")
            if (uiState.successMessage != null) {
                viewModel.clearSuccessMessage()
            }
        }
        Button(
            onClick = {
                viewModel.signUp(context, onNavigateSignIn)
            },
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
                text = "Đăng Ký",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

        }
        SignInText(
            onTextClicked = { onNavigateSignIn() },
            normalText = R.string.have_account,
            clickableText = R.string.login_title,
//            textAnnomation = R.string.signin_annotation,
            modifier = Modifier.navigationBarsPadding()
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
fun LoginTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPasswordField: Boolean = false,
    @DrawableRes leadingIcon: Int

) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(66.dp)
            .border(
                1.dp, Color(0xFF7D32A8),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(4.dp),
        textStyle = TextStyle(
            textAlign = TextAlign.Start,
            color = Color(0xFF7D32A8),
            fontSize = 16.sp
        ),
        placeholder = {
            Text(
                text = placeholder,
                color = Color.Gray,
//                modifier = Modifier.padding(vertical = 4.dp)
            )
        },
        leadingIcon = {
            Icon(painter = painterResource(id = leadingIcon), contentDescription = "")
        },
        trailingIcon = {
            if (isPasswordField) {
                val image = if (passwordVisible) {
                    painterResource(id = R.drawable.eyeoff)
                } else {
                    painterResource(id = R.drawable.eye)
                }

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = image, contentDescription = null)
                }
            }
        },
        visualTransformation = if (isPasswordField && !passwordVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPasswordField) KeyboardType.Password else KeyboardType.Text
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginFields() {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column {
        LoginTextField(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            value = username,
            onValueChange = { username = it },
            placeholder = "Username",
            isPasswordField = false,
            leadingIcon = R.drawable.icon_user_outlined
        )
        LoginTextField(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            value = password,
            onValueChange = { password = it },
            placeholder = "Password",
            isPasswordField = true,
            leadingIcon = R.drawable.icon_padlock_filled
        )
    }
}

@Preview("LightMode", showBackground = true)
@Preview("DarkMode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSignUpScreen() {
    StoreAppTheme {
        SignUpScreen(onNavigateSignIn = {})
    }
}
