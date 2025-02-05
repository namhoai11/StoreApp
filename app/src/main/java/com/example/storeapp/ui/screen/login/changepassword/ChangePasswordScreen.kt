package com.example.storeapp.ui.screen.login.changepassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.storeapp.R
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.screen.login.signup.LoginTextField

object ChangePasswordDestination : NavigationDestination {
    override val route = "change_password"
    override val titleRes = R.string.change_password
}
@Preview
@Composable
fun ChangePasswordScreen() {
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
//        Text(
//            text = "Welcome to Commerc",
//            fontSize = 30.sp,
//            fontStyle = FontStyle.Italic,
//            fontWeight = FontWeight.Bold,
//            color = Color(android.graphics.Color.parseColor("#7d32a8"))
//        )
        var pass by remember { mutableStateOf("") }
        LoginTextField(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            value = pass,
            onValueChange = { pass = it },
            placeholder = "Mật khẩu cũ",
            isPasswordField = true,
            leadingIcon = R.drawable.icon_padlock_filled
        )
        LoginTextField(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            value = pass,
            onValueChange = { pass = it },
            placeholder = "Mật khẩu mới",
            isPasswordField = true,
            leadingIcon = R.drawable.icon_padlock_filled
        )
        LoginTextField(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            value = pass,
            onValueChange = { pass = it },
            placeholder = "Nhập lại mật khẩu mới",
            isPasswordField = true,
            leadingIcon = R.drawable.icon_padlock_filled
        )
        Button(
            onClick = { /*TODO*/ },
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
//        Text(
//            text = "Don't remember password? Click here",
//            Modifier.padding(top = 8.dp, bottom = 8.dp),
//            fontSize = 14.sp,
//            color = Color(0xFF7D32A8),
//        )
//        Row() {
//            Image(
//                painter = painterResource(id = R.drawable.google),
//                contentDescription = null,
//                Modifier.padding(8.dp)
//            )
//            Image(
//                painter = painterResource(id = R.drawable.twitter),
//                contentDescription = null,
//                Modifier.padding(8.dp)
//            )
//            Image(
//                painter = painterResource(id = R.drawable.facebook),
//                contentDescription = null,
//                Modifier.padding(8.dp)
//            )
//        }
        Image(
            painter = painterResource(id = R.drawable.bottom_background),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxWidth()
        )
    }
}