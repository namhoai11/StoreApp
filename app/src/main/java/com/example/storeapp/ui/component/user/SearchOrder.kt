﻿package com.example.storeapp.ui.component.user

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.storeapp.R
import com.example.storeapp.ui.theme.StoreAppTheme

@Composable
fun SearchOrder(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit = {}
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }

    TextField(
        value = searchQuery,
        onValueChange = { text ->
            searchQuery = text
            onSearch(text)
        },
        trailingIcon = {
            Icon(
                painter = painterResource(R.drawable.search_icon_fix),
                contentDescription = "Search",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .scale(1f)
                    .height(50.dp),
                tint = MaterialTheme.colorScheme.outline,
            )
        },
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFE3E3E3),
            unfocusedContainerColor = Color(0xFFE3E3E3),
            focusedIndicatorColor = Color(0xFFE3E3E3),
            unfocusedIndicatorColor = Color(0xFFE3E3E3),
        ),
        placeholder = {
            Text(
                text = "Nhập mã đơn hàng",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .offset(y = (-1.4).dp)
            )
        },
        modifier = modifier
            .border(
                width = 3.dp,
                color = Color("#D1D5DB".toColorInt()),
                shape = RoundedCornerShape(12.dp)
            )
            .fillMaxWidth()
//            .height(50.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchPreview() {
    StoreAppTheme(dynamicColor = false) {
        SearchOrder(
            modifier = Modifier
                .padding(16.dp)
        )
    }
}