package com.example.storeapp.ui.component.admin

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.storeapp.model.CategoryModel
import com.example.storeapp.ui.theme.StoreAppTheme
import com.google.firebase.Timestamp

@Composable
fun CategoryManagementList(
    categoryList: List<CategoryModel>
) {
    LazyColumn(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categoryList) { item ->
            CategoryManagementItem(item = item)
        }
    }
}

@Composable
fun CategoryManagementItem(
    item: CategoryModel,
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = (item.imageUrl), contentDescription = item.name,
                modifier = Modifier
                    .size(80.dp),
                contentScale = ContentScale.Inside,
            )
            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                    ) {
                        Text(
                            text = item.name,
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = item.id.toString(),
                            color = Color.Gray,
//                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
                HorizontalDivider(
                    thickness = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "So san pham:",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    )
                }
            }
        }

    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewCategoryManagementList() {
    val sampleCategories = listOf(
        CategoryModel(
            id = 0,
            name = "PC",
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat1.png?alt=media&token=e3988db7-b935-495a-abbb-89a1b0aa5e0e",
            createdAt = Timestamp.now(),
            updatedAt = Timestamp.now(),
        ),
        CategoryModel(
            id = 0,
            name = "PC",
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat1.png?alt=media&token=e3988db7-b935-495a-abbb-89a1b0aa5e0e",
            createdAt = Timestamp.now(),
            updatedAt = Timestamp.now(),
        ),
        CategoryModel(
            id = 0,
            name = "PC",
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat1.png?alt=media&token=e3988db7-b935-495a-abbb-89a1b0aa5e0e",
            createdAt = Timestamp.now(),
            updatedAt = Timestamp.now(),
        )
    )
    StoreAppTheme {
        CategoryManagementList(sampleCategories)
    }
}
@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewCategoryManagementItem() {
    val sampleItem = CategoryModel(
        id = 0,
        name = "PC",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat1.png?alt=media&token=e3988db7-b935-495a-abbb-89a1b0aa5e0e",
        createdAt = Timestamp.now(),
        updatedAt = Timestamp.now(),
    )
    StoreAppTheme {
        CategoryManagementItem(sampleItem)
    }
}