package com.example.storeapp.ui.screen.address.add_address

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.storeapp.R
import com.example.storeapp.ui.component.user.AnimatedShimmerDetailAddress
import com.example.storeapp.ui.uistate.AddAddressUiState
import com.google.maps.android.compose.CameraPositionState

class AddAddressScreen {
}

@Composable
fun BottomSheetContent(
    locationName: String,
    uiState: AddAddressUiState,
//    viewModel: AddAddressViewModel,
    cameraPositionState: CameraPositionState,
    recipientName: String,
    onRecipientNameChange: (String) -> Unit,
    onConfirm: () -> Unit
) {
    Column(
        modifier = Modifier
            .height(265.dp)
            .fillMaxWidth()
            .padding(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 8.dp
            )
    ) {
        if (uiState.locatonLoading) {
            Text(
                text = "Recipient Name",
//                    fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            BasicTextField(
                value = recipientName,
                onValueChange = onRecipientNameChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .drawBehind {
                        val strokeWidth = 2.dp.toPx()
                        val y = size.height - strokeWidth / 2
                        drawLine(
                            color = Color(0xFFCAC8C8),
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = strokeWidth
                        )
                    },
                textStyle = TextStyle(
//                        fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                ),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                    ) {
                        if (recipientName.isEmpty()) {
                            Text(
                                text = "Enter recipient's name",
                                style = TextStyle(
//                                        fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Gray
                                )
                            )
                        }
                        innerTextField()
                    }
                }
            )
            Text(
                text = "Detail Address",
//                    fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row {
                Icon(
                    painter = painterResource(R.drawable.icon_circle_dot_filled),
                    contentDescription = "",
                    tint = Color("#FF9100".toColorInt()),
                    modifier = Modifier.padding(end = 16.dp)
                )
                Box(modifier = Modifier.fillMaxSize()) {
                    AnimatedShimmerDetailAddress()

                    val currentLatLng = cameraPositionState.position.target
//                        viewModel.getInitialLocationName(currentLatLng)
                }
            }
            Button(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .height(55.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
//                        fontFamily = poppinsFontFamily,
                    text = "Confirmation",
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        } else {
            Text(
                text = "Recipient Name",
//                    fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            BasicTextField(
                value = recipientName,
                onValueChange = onRecipientNameChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .drawBehind {
                        val strokeWidth = 2.dp.toPx()
                        val y = size.height - strokeWidth / 2
                        drawLine(
                            color = Color(0xFFCAC8C8),
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = strokeWidth
                        )
                    },
                textStyle = TextStyle(
//                        fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                ),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                    ) {
                        if (recipientName.isEmpty()) {
                            Text(
                                text = "Enter recipient's name",
                                style = TextStyle(
//                                        fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Gray
                                )
                            )
                        }
                        innerTextField()
                    }
                }
            )
            Text(
                text = "Detail Address",
//                    fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row {
                Icon(
                    painter = painterResource(R.drawable.icon_circle_dot_filled),
                    contentDescription = "",
                    tint = Color("#FF9100".toColorInt()),
                    modifier = Modifier.padding(end = 16.dp)
                )
                Text(
                    modifier = Modifier.height(60.dp),
                    text = locationName,
                    maxLines = 3,
//                        fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                )
            }
            Button(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .height(55.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
//                        fontFamily = poppinsFontFamily,
                    text = "Confirmation",
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }
    }
}