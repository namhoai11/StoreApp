﻿package com.example.storeapp.ui.component.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.storeapp.ui.theme.StoreAppTheme

@Composable
fun AddressItemScreen(
    name: String,
    address: String,
    modifier: Modifier = Modifier
) {
    Card(
        border = BorderStroke(
            width = 1.dp,
            color = Color.LightGray
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(125.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 80.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(20.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                MaterialTheme.colorScheme.primary
                            )
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    maxLines = 3,
                    text = address,
                    fontSize = 14.sp,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Edit",
                    fontSize = 14.sp,
                )
                Spacer(modifier = Modifier.size(4.dp))
//                Icon(
//                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
//                    contentDescription = "",
//                    modifier = Modifier
//                        .size(20.dp)
//                )
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun AddressItemPreview() {
    StoreAppTheme(
        dynamicColor = false
    ) {
        AddressItemScreen(
            name = "Dwi Azi Prasetya",
            address = "Jl. Durian No. 123, Banyubiru " +
                    "Kab. Semarang, Jawa Tengah " +
                    "Indonesia, 50123",
        )
    }
}

@Composable
fun AddressItemScreen2(
    name: String,
    address: String,
    isSelected: Boolean,
    onDeleteClick: () -> Unit,
    onChooseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(
            width = 1.dp,
            color = Color.LightGray
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = modifier
            .clickable { onChooseClick() }
            .fillMaxWidth()
            .height(150.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 80.dp)
            ) {
                Text(
                    text = name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    maxLines = 3,
                    text = address,
                    fontSize = 14.sp,
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = onChooseClick
                )
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Address",
                    modifier = Modifier
                        .clickable { onDeleteClick() }
                )
            }
        }
    }
}


@Preview(
    showBackground = true
)
@Composable
private fun AddressItemPreview2() {
    StoreAppTheme(
        dynamicColor = false
    ) {
        AddressItemScreen2(
            name = "Dwi Azi Prasetya",
            address = "Jl. Durian No. 123, Banyubiru " +
                    "Kab. Semarang, Jawa Tengah " +
                    "Indonesia, 50123",
            isSelected = true,
            onDeleteClick = {},
            onChooseClick = {}
        )
    }
}