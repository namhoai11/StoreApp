package com.example.storeapp.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import coil.compose.rememberAsyncImagePainter
import com.example.storeapp.R
import com.example.storeapp.model.ItemsModel
import com.example.storeapp.ui.theme.StoreAppTheme


@Composable
fun CartList(
    cartItems: ArrayList<ItemsModel>,
) {
    LazyColumn(
        Modifier
            .padding(top = 8.dp)
            .heightIn(max = 500.dp) // Đặt chiều cao tối đa
    )
    {
        items(cartItems) { item ->
            CartItem(
                cartItem = item,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun CartItem(
    cartItem: ItemsModel,
    modifier: Modifier = Modifier,
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
//                .padding(top = 8.dp, bottom = 8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = cartItem.picUrl[0]),
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
                    text = cartItem.title,
                    modifier = Modifier
                        .padding(start = 8.dp)
                )
                Text(
                    text = "$${cartItem.price}",
                    color = colorResource(id = R.color.purple),
                    modifier = Modifier
                        .padding(start = 8.dp, bottom = 8.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth() // Đảm bảo Row chiếm toàn bộ chiều rộng
//                    .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = "$${cartItem.price}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 8.dp, top = 8.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    NumberInCart(numberInCart = 2)
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
fun CartItemsPreview() {
    StoreAppTheme {
        val item = ItemsModel(
            id = 1,
            title = "Business Laptop",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed  do eiusmod tempor incididunt ut labore et dolore magna  aliqua. Ut enim ad minim veniam, quis nostrud exercitation  ullamco laboris nisi ut aliquip ex ea commodo consequat.  Duis aute irure dolor in reprehenderit in voluptate velit esse  cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat  cupidatat non proident, sunt in culpa qui officia deserunt .Excepteur sint occaecat",
            picUrl = arrayListOf(
                "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_1.png?alt=media&token=fb49a7c9-3094-4f5c-9ea6-b8365cd86323",
                "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_2.png?alt=media&token=3f826014-4808-4387-af6f-22dc7ddd4780",
                "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_3.png?alt=media&token=d4ab793a-cb72-45ab-ae43-8db69adaaeba",
                "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_4.png?alt=media&token=dfb10462-9138-471a-b34a-537bc7f5b7c8",
                "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_5.png?alt=media&token=2bfd17ef-d8c5-409e-8d6c-2d9e57d394c4"
            ),
            model = arrayListOf(
                "core i3",
                "core i5",
                "core i7"
            ),
            price = 550.0,
            rating = 4.7,
            showRecommended = true,
            categoryId = "0"
        )
        CartItem(
            item
        )
    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CartListPreview() {
    StoreAppTheme {
        val listItem = arrayListOf(
            ItemsModel(
                id = 1,
                title = "Business Laptop",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed  do eiusmod tempor incididunt ut labore et dolore magna  aliqua. Ut enim ad minim veniam, quis nostrud exercitation  ullamco laboris nisi ut aliquip ex ea commodo consequat.  Duis aute irure dolor in reprehenderit in voluptate velit esse  cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat  cupidatat non proident, sunt in culpa qui officia deserunt .Excepteur sint occaecat",
                picUrl = arrayListOf(
                    "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_1.png?alt=media&token=fb49a7c9-3094-4f5c-9ea6-b8365cd86323",
                    "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_2.png?alt=media&token=3f826014-4808-4387-af6f-22dc7ddd4780",
                    "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_3.png?alt=media&token=d4ab793a-cb72-45ab-ae43-8db69adaaeba",
                    "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_4.png?alt=media&token=dfb10462-9138-471a-b34a-537bc7f5b7c8",
                    "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_5.png?alt=media&token=2bfd17ef-d8c5-409e-8d6c-2d9e57d394c4"
                ),
                model = arrayListOf(
                    "core i3",
                    "core i5",
                    "core i7"
                ),
                price = 550.0,
                rating = 4.7,
                showRecommended = true,
                categoryId = "0"
            ),
            ItemsModel(
                id = 2,
                title = "Business Laptop",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed  do eiusmod tempor incididunt ut labore et dolore magna  aliqua. Ut enim ad minim veniam, quis nostrud exercitation  ullamco laboris nisi ut aliquip ex ea commodo consequat.  Duis aute irure dolor in reprehenderit in voluptate velit esse  cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat  cupidatat non proident, sunt in culpa qui officia deserunt .Excepteur sint occaecat",
                picUrl = arrayListOf(
                    "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_1.png?alt=media&token=fb49a7c9-3094-4f5c-9ea6-b8365cd86323",
                    "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_2.png?alt=media&token=3f826014-4808-4387-af6f-22dc7ddd4780",
                    "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_3.png?alt=media&token=d4ab793a-cb72-45ab-ae43-8db69adaaeba",
                    "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_4.png?alt=media&token=dfb10462-9138-471a-b34a-537bc7f5b7c8",
                    "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_5.png?alt=media&token=2bfd17ef-d8c5-409e-8d6c-2d9e57d394c4"
                ),
                model = arrayListOf(
                    "core i3",
                    "core i5",
                    "core i7"
                ),
                price = 550.0,
                rating = 4.7,
                showRecommended = true,
                categoryId = "0"
            )
        )
        CartList(
            listItem
        )
    }
}
