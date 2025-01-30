package com.example.storeapp.ui.component

import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.storeapp.data.local.DataDummy
import com.example.storeapp.model.UserLocationModel
import com.example.storeapp.ui.theme.StoreAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AddressContent(
    modifier: Modifier = Modifier,
    state: List<UserLocationModel>,
    scope: CoroutineScope,
    selectedItemId: Int?,
    onSelectedItem: (Int) -> Unit,
    onDeletedItem: (Int) -> Unit,
    onConfirmationClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            if (state.isEmpty()) {
                Box(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        .fillMaxWidth()
                        .height(200.dp),
                ) {
                    Text(
                        text = "No Address Found",
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            } else {
                LazyColumn {
                    items(items = state, key = { it.id }) { userLocation ->
                        AddressItemScreen2(
                            name = userLocation.name,
                            address = userLocation.address,
                            isSelected = selectedItemId == userLocation.id,
                            onChooseClick = { onSelectedItem(userLocation.id) },
                            onDeleteClick = { onDeletedItem(userLocation.id) },
//                            modifier = Modifier.animateItemPlacement(tween(100))
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(top = 16.dp)) {
            Card(
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.LightGray
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            scope.launch {
                            }
                        }
                ) {
                    Text(
                        text = "Add New Address",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.Center)
                    )
                }
            }

            Spacer(modifier = Modifier.size(16.dp))

            Button(
                enabled = selectedItemId != null,
                modifier = Modifier
                    .height(55.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                onClick = onConfirmationClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Confirmation",
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun AddressContentPreview() {
    StoreAppTheme(dynamicColor = false) {
        AddressContent(
            state = DataDummy.dummyUserLocation,
//            navHostController = rememberNavController(),
            scope = rememberCoroutineScope(),
            selectedItemId = 2,
            onSelectedItem = {},
            onDeletedItem = {},
            onConfirmationClick = {}
        )
    }
}