package com.example.storeapp.ui.component.user

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
    modifier: Modifier = Modifier // Thêm modifier vào đây
) {
    Row(
        modifier = modifier.padding(top = 16.dp), // Áp dụng modifier vào Row
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = normalText),
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(id = clickableText),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.purple),
            modifier = Modifier.clickable { onTextClicked() }
        )
    }
}


@Preview("LightMode", showBackground = true)
//@Preview("DarkMode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSignInText() {
    StoreAppTheme {
        SignInText(
            normalText = R.string.non_account,
            clickableText = R.string.signup_title,
//            textAnnomation = R.string.signup_annotation,
            onTextClicked = {}
        )
    }
}