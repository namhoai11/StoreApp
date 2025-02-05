package com.example.storeapp.ui.component.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.storeapp.R


@Preview
@Composable
fun BottomMenu(modifier: Modifier=Modifier, onItemClick: () -> Unit={}) {
    Row(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
            .background(
                colorResource(id = R.color.purple),
                shape = RoundedCornerShape(10.dp)
            ),
//            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        BottomMenuItem(icon = painterResource(id = R.drawable.btn_1), text = "Explorer")
        BottomMenuItem(
            icon = painterResource(id = R.drawable.btn_2),
            text = "Cart",
            onItemClick = onItemClick
        )
        BottomMenuItem(icon = painterResource(id = R.drawable.btn_3), text = "Favorite")
        BottomMenuItem(icon = painterResource(id = R.drawable.btn_4), text = "Oders")
        BottomMenuItem(icon = painterResource(id = R.drawable.btn_5), text = "Profile")

    }
}


@Composable
fun BottomMenuItem(icon: Painter, text: String, onItemClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .height(60.dp)
            .clickable { onItemClick.invoke() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
//        Icon(imageVector = icon, contentDescription = text, tint = Color.White)
        Image(
            painter = icon,
            contentDescription = text,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier.size(24.dp)
        )
        Text(text = text, color = Color.White, fontSize = 10.sp)

    }

}