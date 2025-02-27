package com.example.storeapp.ui.screen.checkout.successpayment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.storeapp.R
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.function.formatCurrency2
import com.example.storeapp.ui.component.function.timestampToDateString
import com.example.storeapp.ui.component.user.SuccessAnimation
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.theme.StoreAppTheme


object SuccessPaymentDestination : NavigationDestination {
    override val route = "successpayment?orderId={orderId}"
    override val titleRes = R.string.successpaymentmethod_title
    fun createRoute(orderId: String?): String {
        return "successpayment?orderId=$orderId"
    }
}
@Composable
fun SuccessPaymentScreen(
    modifier: Modifier = Modifier,
    onNavigateHome: () -> Unit,
    viewModel: SuccessPaymentViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    val currentOrder = uiState.currentOrder
    Box(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp
                )
                .align(Alignment.TopCenter)
        ) {
            SuccessAnimation(
                modifier = Modifier
                    .size(250.dp)
            )
            Text(
                modifier = Modifier,
//                    .offset(y = (-30).dp),
                fontSize = 24.sp,
                text = "Payment Success",
                fontWeight = FontWeight.Medium,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                if (currentOrder != null) {
                    Text(
                        text = formatCurrency2(currentOrder.totalPrice),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier.padding(
                    top = 16.dp,
                    bottom = 16.dp
                ),
                color = Color(0xFFE3E3E3)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "code",
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.weight(1f)
                )
                if (currentOrder != null) {
                    Text(
                        text = currentOrder.orderCode,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Số sản phẩm",
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.weight(1f)
                )
                if (currentOrder != null) {
                    Text(
                        text = "${currentOrder.products.sumOf { it.quantity }}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Địa chỉ",
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.weight(1f)
                )
                if (currentOrder != null) {
                    Text(
                        text = currentOrder.address.userName + " - " +currentOrder.address.street + " - " + currentOrder.address.ward + " - " + currentOrder.address.district + " - " + currentOrder.address.province,
                        fontSize = 16.sp,
                        maxLines = 3,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)

                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Phương thức thanh toán",
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.weight(1f)
                )
                if (currentOrder != null) {
                    Text(
                        text = currentOrder.paymentMethod,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Thời gian",
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.weight(1f)
                )
                if (currentOrder != null) {
                    Text(
                        text = timestampToDateString(currentOrder.updatedAt),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }
        Button(
            modifier = Modifier
                .padding(
                    top = 8.dp,
                    bottom = 8.dp,
                    start = 16.dp,
                    end = 16.dp
                )
                .height(55.dp)
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            onClick = {
                onNavigateHome()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Trở về trang chủ",
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SuccessPaymentPreview() {
    StoreAppTheme(dynamicColor = false) {
        SuccessPaymentScreen(
            onNavigateHome = {}
        )
    }
}