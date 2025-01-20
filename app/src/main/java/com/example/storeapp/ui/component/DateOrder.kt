package com.example.storeapp.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.storeapp.R
import com.example.storeapp.ui.theme.StoreAppTheme

@Composable
fun DateOrder(
    modifier: Modifier = Modifier,
    imageId: Int,
    dateOrderText: String,
    date: String = "",

) {
    Row(
        modifier = modifier
            .padding(bottom = 8.dp, top = 8.dp, end = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = "calendarIcon",
            modifier = Modifier
                .size(20.dp)
                .aspectRatio(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = dateOrderText,
            color = Color.Gray,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = date,
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DateOrderPreview() {
    StoreAppTheme {
        DateOrder(
            imageId = R.drawable.icon_calendar,
            dateOrderText = "Ngày xuất kho:",
            date = "02/04/2024"
        )
    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DateOrder1Preview() {
    StoreAppTheme {
        DateOrder(
            imageId = R.drawable.icon_pinlocation,
            dateOrderText = "Tp.Hồ Chí Minh, Việt Nam",
//            date = "02/04/2024"
        )
    }
}