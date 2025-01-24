package com.example.storeapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.example.storeapp.R
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun GoogleMapContent(
    cameraPositionState: CameraPositionState,
) {
    Box(modifier = Modifier
        .fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = Color.White,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_circle_dot_filled),
                    contentDescription = "Custom Pin",
                    modifier = Modifier
                        .size(40.dp),
                    tint = Color("#FF9100".toColorInt())
                )
            }

            Box(
                modifier = Modifier
                    .size(10.dp)
                    .align(Alignment.BottomCenter)
                    .offset(y = 15.dp)
                    .background(
                        color = Color("#FF9100".toColorInt()),
                        shape = CircleShape
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGoogleMapContent() {
    // Giả lập trạng thái camera với một vị trí mặc định
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(10.8231, 106.6297), // Tọa độ của TP.HCM
            12f // Độ zoom
        )
    }

    GoogleMapContent(
        cameraPositionState = cameraPositionState
    )
}

