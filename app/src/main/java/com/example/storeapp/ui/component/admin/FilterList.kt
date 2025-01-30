package com.example.storeapp.ui.component.admin

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.storeapp.R
import com.example.storeapp.data.local.OrderStatusProvider
import com.example.storeapp.ui.component.CategoryItem
import com.example.storeapp.ui.theme.StoreAppTheme
import java.util.logging.Filter

@Composable
fun FilterList(
    modifier: Modifier = Modifier,
    filterList: List<String>,
    onFilterSelected: (String) -> Unit,
) {
    var selectedIndex by remember {
        mutableStateOf(0)
    }
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
//        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(filterList.size) { index ->
            FilterItem(
                textItem = filterList[index],
                isSelected = selectedIndex == index,
                onItemClicked = {
                    selectedIndex = index
                    // Gọi callback để truyền `id` ra ngoài
                    onFilterSelected(filterList[index])
                }
            )
        }
    }
}

@Composable
fun FilterItem(
    isSelected: Boolean,
    textItem: String,
    onItemClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .clickable(onClick = onItemClicked)
            .border(
                width = 1.dp,
//                color = Color("#D1D5DB".toColorInt()),
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(3.dp)
            ),
        shape = RoundedCornerShape(3.dp),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    shape = RoundedCornerShape(5.dp) // Đảm bảo shape giống với Card
                )
        ) {
            Text(
                text = textItem,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Preview("LightTheme", showBackground = true)
@Preview("DarkTheme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewFilterItem() {
    StoreAppTheme {
        FilterItem(true, "Đang giao", {})
    }
}

@Preview("LightTheme", showBackground = true)
@Preview("DarkTheme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewFilterList() {
    StoreAppTheme {
        val filterList = OrderStatusProvider.orderStatusList
        FilterList(
            modifier = Modifier,
            filterList,
            {}
        )
    }
}