package com.example.storeapp.ui.component.user

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.storeapp.R
import com.example.storeapp.ui.theme.StoreAppTheme

@Composable
fun SignInText(
    onTextClicked: () -> Unit,
    @StringRes normalText: Int,
    @StringRes clickableText: Int,
    @StringRes textAnnomation: Int,

) {
//    val normalText = stringResource(id = R.string.non_account)
//    val clickableText = stringResource(id = R.string.signup_title)

    val annotatedString = buildAnnotatedString {
        append(stringResource(id = normalText))
        append(" ")
        pushStringAnnotation(tag = stringResource(id = clickableText), annotation = stringResource(
            id = textAnnomation
        ))
        withStyle(
            style = SpanStyle(
                color = colorResource(id = R.color.purple),
                fontWeight = FontWeight.Bold
            )
        ) {
            append(stringResource(id = clickableText))
        }
        pop()
    }

    Text(
        text = annotatedString,
        modifier = Modifier
            .padding(top = 16.dp)
            .clickable {
                onTextClicked()
            },
        style = TextStyle(fontSize = 18.sp, textAlign = TextAlign.Center)
    )
}


@Preview("LightMode", showBackground = true)
//@Preview("DarkMode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSignInText() {
    StoreAppTheme {
        SignInText(
            normalText = R.string.non_account,
            clickableText = R.string.signup_title,
            textAnnomation = R.string.signup_annotation,
            onTextClicked = {}
        )
    }
}