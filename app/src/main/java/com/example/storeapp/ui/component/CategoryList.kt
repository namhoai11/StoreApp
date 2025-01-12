package com.example.storeapp.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.storeapp.R
import com.example.storeapp.model.CategoryModel

@Composable
fun CategoryList(
    categories: List<CategoryModel>,
    onCategorySelected: (String) -> Unit = {},
    isShowRecommend: Boolean = true
) {
    var selectedIndex by remember {
        mutableStateOf(0)
    }
//    val context = LocalContext.current
    // Thêm "All" hoặc "Recommend" vào đầu danh sách nếu được yêu cầu
    val extendedCategories = if (isShowRecommend) {
        listOf(
            CategoryModel(
                id = -1,
                title = "Recommend",
                picUrl = "https://firebasestorage.googleapis.com/v0/b/commerc-b4186.appspot.com/o/recommend.png?alt=media&token=0d69a7fa-9d97-489a-bfbc-f5d89c3f35fe"
            ) // Item tĩnh
        ) + categories
    } else {
        listOf(
            CategoryModel(
                id = -2,
                title = "All",
                picUrl = "https://firebasestorage.googleapis.com/v0/b/commerc-b4186.appspot.com/o/All.png?alt=media&token=f68f85f0-8388-4f60-b50b-fe952f2f17dd"
            ) // Item tĩnh
        ) + categories
    }
    // Gọi onCategorySelected ngay khi danh sách được khởi tạo
    LaunchedEffect(extendedCategories, selectedIndex) {
        onCategorySelected(extendedCategories[selectedIndex].id.toString())
    }
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp)
    ) {
        items(extendedCategories.size) { index ->
            CategoryItem(
                item = extendedCategories[index],
                isSelected = selectedIndex == index,
                onItemClick = {
                    selectedIndex = index
                    // Gọi callback để truyền `id` ra ngoài
                    onCategorySelected(extendedCategories[index].id.toString())
                }
            )
        }
    }
}

@Composable
fun CategoryItem(
    item: CategoryModel,
    isSelected: Boolean,
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onItemClick)
            .background(
                color = if (isSelected) colorResource(id = R.color.purple) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = (item.picUrl), contentDescription = item.title,
            modifier = Modifier
                .size(45.dp)
                .background(
                    color = if (isSelected) Color.Transparent else colorResource(id = R.color.lightGrey),
                    shape = RoundedCornerShape(8.dp)
                ),
            contentScale = ContentScale.Inside,
            colorFilter = if (isSelected) {
                ColorFilter.tint(Color.White)
            } else {
                ColorFilter.tint(Color.Black)
            }
        )
        if (isSelected) {
            Text(
                text = item.title,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewCategoryItem() {
    val sampleItem = CategoryModel(
        id = 0,
        title = "PC",
        picUrl = "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat1.png?alt=media&token=e3988db7-b935-495a-abbb-89a1b0aa5e0e"
    )

    CategoryItem(
        item = sampleItem,
        isSelected = true,
        onItemClick = {
            // Debug: Handle item click
            println("Item clicked: ${sampleItem.title}")
        }
    )
}
@Preview(showBackground = true)
@Composable
fun PreviewCategoryList() {
    val sampleCategories = listOf(
        CategoryModel(id = 0, title = "PC", picUrl = "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat1.png?alt=media&token=e3988db7-b935-495a-abbb-89a1b0aa5e0e"),
        CategoryModel(id = 1, title = "PC", picUrl = "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat1.png?alt=media&token=e3988db7-b935-495a-abbb-89a1b0aa5e0e"),
        CategoryModel(id = 2, title = "PC", picUrl = "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat1.png?alt=media&token=e3988db7-b935-495a-abbb-89a1b0aa5e0e")
    )

    CategoryList(
        categories = sampleCategories,
        onCategorySelected = { categoryId ->
            // Debug: Print selected category
            println("Selected Category ID: $categoryId")
        },
        isShowRecommend = true
    )
}
