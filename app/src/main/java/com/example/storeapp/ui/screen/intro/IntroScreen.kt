package com.example.storeapp.ui.screen.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.storeapp.R
import com.example.storeapp.ui.component.user.SignInText
import com.example.storeapp.ui.navigation.NavigationDestination

object IntroDestination : NavigationDestination {
    override val route = "intro"
    override val titleRes = R.string.intro_title
}

@Composable
@Preview
fun IntroScreen(
    onNavigateHome: () -> Unit = {},
    onNavigateSignIn: () -> Unit = {},
    onNavigateSignUp: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.intro_logo),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 48.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(id = R.string.intro_title),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(id = R.string.intro_sub_title),
            modifier = Modifier.padding(top = 16.dp),
            color = Color.DarkGray,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
        Button(
            onClick = { onNavigateHome() },
            modifier = Modifier
                .padding(
                    horizontal = 32.dp,
                    vertical = 16.dp
                )
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor =
                colorResource(id = R.color.purple)
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = stringResource(id = R.string.letgo),
                color = Color.White,
                fontSize = 18.sp
            )
        }
        Button(
            onClick = { onNavigateSignIn() },
            modifier = Modifier
                .padding(
                    horizontal = 32.dp,
//                    vertical = 16.dp
                )
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor =
                colorResource(id = R.color.purple)
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = " Sign in",
                color = Color.White,
                fontSize = 18.sp
            )
        }

//        Text(
//            text = stringResource(id = R.string.sign),
//            textAlign = TextAlign.Center,
//            modifier = Modifier.padding(top = 16.dp),
//            fontSize = 18.sp
//        )
        // Các thành phần khác
        SignInText(
            normalText = R.string.non_account,
            clickableText = R.string.signup_title,
//            textAnnomation = R.string.signup_annotation,
            onTextClicked = {
                onNavigateSignUp()
            },
            modifier = Modifier.navigationBarsPadding()
        )
    }
}

//@Composable
//fun SignInText(onSignUpClick: () -> Unit) {
//    val normalText = stringResource(id = R.string.sign)
//    val clickableText = "SignUp"
//
//    val annotatedString = buildAnnotatedString {
//        append(normalText)
//        withStyle(
//            style = SpanStyle(
//                color = colorResource(id = R.color.purple),
//                fontWeight = FontWeight.Bold
//            )
//        ) {
//            pushStringAnnotation(tag = "SignUp", annotation = "sign_up")
//            append(clickableText)
//            pop()
//        }
//    }
//    ClickableText(
//        text = annotatedString,
//        onClick = { offset ->
//            annotatedString.getStringAnnotations(tag = "SignUp", start = offset, end = offset)
//                .firstOrNull()?.let {
//                    onSignUpClick()
//                }
//        },
//        modifier = Modifier.padding(top = 16.dp),
//        style = TextStyle(fontSize = 18.sp, textAlign = TextAlign.Center)
//    )
//}
