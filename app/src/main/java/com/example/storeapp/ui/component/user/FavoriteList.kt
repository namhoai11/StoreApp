package com.example.storeapp.ui.component.user

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.storeapp.R
import com.example.storeapp.model.WishListModel
import com.example.storeapp.ui.theme.StoreAppTheme

@Composable
fun FavoriteList(
    favItems: List<WishListModel>,
    onFavIconClick: (String) -> Unit = {},
    onFavItemClick: (String) -> Unit = {},

) {
    LazyColumn(
        Modifier
            .padding(top = 8.dp)
            .heightIn(max = 500.dp) // Đặt chiều cao tối đa
    )
    {
        items(favItems) { item ->
            FavItem(
                favItem = item,
                onFavIconClick,
                onFavItemClick,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}


@Composable
fun FavItem(
    favItem: WishListModel,
    onFavIconClick: (String) -> Unit = {},
    onFavItemClick: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.clickable {
            onFavItemClick(favItem.productId)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
//                .padding(top = 8.dp, bottom = 8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = favItem.productImage),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        colorResource(id = R.color.lightGrey),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(8.dp)
            )
            Column {
                Text(
                    text = favItem.productName,
                    modifier = Modifier
                        .padding(start = 8.dp)
                )
                Text(
                    text = favItem.productCategory,
                    modifier = Modifier
                        .padding(start = 8.dp, bottom = 8.dp)
                )
                Text(
                    text = "Quantity: ${favItem.productQuantity}",
                    modifier = Modifier
                        .padding(start = 8.dp, bottom = 8.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth() // Đảm bảo Row chiếm toàn bộ chiều rộng
                ) {
                    Text(
                        text = "$${favItem.productPrice}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 8.dp, top = 8.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(R.drawable.icon_favourite_filled_red),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .padding(
                                end = 8.dp,
                                bottom = 4.dp,

                                )
                            .align(Alignment.Bottom)
                            .clickable {
                                onFavIconClick(favItem.productId)
                            }
                    )
                }
            }
        }
    }

}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FavItemPreview() {
    StoreAppTheme {
        val favItem = WishListModel(
            productId = "1",
            productName = "Business Laptop",
            productCategory = "Electronics",
            productImage = "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_1.png?alt=media&token=fb49a7c9-3094-4f5c-9ea6-b8365cd86323",
            productPrice = 550.0,
            productQuantity = 2,
        )
        FavItem(favItem = favItem)
    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FavListPreview() {
    StoreAppTheme {
        val favItems = arrayListOf(
            WishListModel(
                productId = "1",
                productName = "Business Laptop",
                productCategory = "Electronics",
                productImage = "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_1.png?alt=media&token=fb49a7c9-3094-4f5c-9ea6-b8365cd86323",
                productPrice = 550.0,
                productQuantity = 2
            ),
            WishListModel(
                productId = "2",
                productName = "Gaming Laptop",
                productCategory = "Electronics",
                productImage = "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_2.png?alt=media&token=3f826014-4808-4387-af6f-22dc7ddd4780",
                productPrice = 1200.0,
                productQuantity = 2
            )
        )
        FavoriteList(favItems = favItems)
    }
}
