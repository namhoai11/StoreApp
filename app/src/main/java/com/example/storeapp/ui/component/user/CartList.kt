package com.example.storeapp.ui.component.user

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.storeapp.R
import com.example.storeapp.data.local.DataDummy
import com.example.storeapp.model.CartModel
import com.example.storeapp.model.ProductsOnCart
import com.example.storeapp.ui.theme.StoreAppTheme


@Composable
fun CartList(
    cartItems: CartModel,
) {
    LazyColumn(
        Modifier
            .padding(top = 8.dp)
            .heightIn(max = 500.dp) // Đặt chiều cao tối đa
    )
    {
        items(cartItems.products) { item ->
            CartItem(
                cartItem = item,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun CartItem(
    cartItem: ProductsOnCart,
    modifier: Modifier = Modifier,
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
//                .padding(top = 8.dp, bottom = 8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = cartItem.productImage),
                contentDescription = null,
                modifier = Modifier
                    .size(90.dp)
                    .background(
                        colorResource(id = R.color.lightGrey),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(8.dp)
            )
            Column {
                Text(
                    text = cartItem.productName,
                    modifier = Modifier
                        .padding(start = 8.dp)
                )
                Text(
//                    text = "$${cartItem.productPrice}",
                    text = "ProductPrice",
                    color = colorResource(id = R.color.purple),
                    modifier = Modifier
                        .padding(start = 8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth() // Đảm bảo Row chiếm toàn bộ chiều rộng
                        .padding(bottom = 8.dp)
                ) {
                    Text(
//                        text = "$${cartItem.productPrice}",
                        text = "ProductPrice",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 8.dp, top = 4.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    NumberInCart(numberInCart = cartItem.quantity)
                }
            }
        }
    }
}

@Composable
fun NumberInCart(
    numberInCart: Int
) {
    Row(
        modifier = Modifier
            .width(100.dp)
            .background(
                colorResource(id = R.color.lightGrey),
                shape = RoundedCornerShape(10.dp)
            ),
        horizontalArrangement = Arrangement.Center, // Căn giữa các phần tử theo chiều ngang
        verticalAlignment = Alignment.CenterVertically // Căn giữa theo chiều dọc
    ) {
        Box(
            modifier = Modifier
                .padding(2.dp)
                .size(28.dp)
                .background(
                    colorResource(id = R.color.purple),
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Text(
                text = "-",
                color = Color.White,
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        }
        Text(
            text = numberInCart.toString(),
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp) // Thêm khoảng cách giữa số và các nút
        )

        Box(
            modifier = Modifier
                .padding(2.dp)
                .size(28.dp)
                .background(
                    colorResource(id = R.color.purple),
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Text(
                text = "+",
                color = Color.White,
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        }

    }
}

@Composable
fun CartItemMini(
    modifier: Modifier = Modifier,
    productName: String,
    imageId: String,
    price: Double,
    orderCount: Int,
    totalOrder: Int,
    onDetailOrder: () -> Unit,
) {
    val totalPrice = price * orderCount

    Row(
        modifier = modifier
            .padding(
                vertical = 8.dp,
                horizontal = 16.dp
            )
            .height(80.dp)
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.background
            )
    ) {
        AsyncImage(
            modifier = Modifier
                .size(60.dp),
            model = imageId,
            contentDescription = null
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = productName,
//                fontSize = 12.sp,
//                fontWeight = FontWeight.Medium,
                maxLines = 2
            )
            Spacer(modifier = Modifier.weight(1f))
            Row {
                Text(
                    text = "$orderCount x $${"%.2f".format(totalPrice)}",
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
//                if (totalOrder > 1) {
//                    Text(
//                        text = "and ${totalOrder - 1} more",
//                        fontWeight = FontWeight.Normal,
//                        fontSize = 12.sp,
//                        color = MaterialTheme.colorScheme.primary,
//                        modifier = Modifier.clickable { onDetailOrder() }
//                    )
//                }
            }
        }
    }
}

@Composable
fun CartMiniList(
    cartItems: CartModel,
) {
    LazyColumn(
        Modifier
            .padding(top = 8.dp)
            .heightIn(max = 500.dp) // Đặt chiều cao tối đa
    )
    {
        items(cartItems.products) { item ->
            CartItemMini(
                productName = item.productName,
                imageId = item.productImage,
                price = 0.0,
                orderCount = item.quantity,
                totalOrder = 0,
                onDetailOrder = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CartItemMiniPreview() {
    StoreAppTheme(
        dynamicColor = false
    ) {
        CartItemMini(
            productName = "Fjallraven - Foldsack No. 1 Backpack",
            price = 234.0,
            orderCount = 2,
            imageId = "",
            totalOrder = 3,
            onDetailOrder = {}
        )
    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NumberInCartPreview() {
    StoreAppTheme {
        NumberInCart(
            1
        )
    }
}


@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CartItemPreview() {
    StoreAppTheme {
        val item2 = DataDummy.productsOnCart
        CartItem(
            item2
        )
    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CartListPreview() {
    StoreAppTheme {
        val cart = DataDummy.cartItems
        CartList(
            cart
        )
    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CartMiniListPreview() {
    StoreAppTheme {
        val cart = DataDummy.cartItems
        CartMiniList(cartItems = cart)
    }
}

