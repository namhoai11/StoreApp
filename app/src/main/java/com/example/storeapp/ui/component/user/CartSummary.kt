package com.example.storeapp.ui.component.user

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.storeapp.R
import com.example.storeapp.ui.theme.StoreAppTheme

@Composable
fun CartSummary(itemTotal: Double, tax: Double, delivery: Double) {
    val total = itemTotal + tax + delivery
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Item Total:",
                Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.grey)
            )
            Text(text = "$$itemTotal")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Tax:",
                Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.grey)
            )
            Text(text = "$$tax")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
        ) {
            Text(
                text = "Delivery:",
                Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.grey)
            )
            Text(text = "$$delivery")
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(colorResource(id = R.color.grey))
                .padding(vertical = 16.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Total:",
                Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.grey)
            )
            Text(text = "$$total")
        }
        Button(
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.purple)
            ),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Checkout",
                fontSize = 18.sp,
                color = Color.White
            )

        }

    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CartSummaryPreview() {
    StoreAppTheme {
        CartSummary(
            465.0, 12.9, 10.0
        )
    }

}