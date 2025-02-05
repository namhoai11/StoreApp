package com.example.storeapp.ui.component.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.storeapp.ui.theme.StoreAppTheme



@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit = {},
//    onSortChange: (String) -> Unit = {}
) {

//    var isAscending by remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(
            start = 16.dp,
            end = 16.dp,
            top = 24.dp,
            bottom = 8.dp
        )
    ) {
        Text(
            text = "Search",
            modifier = Modifier.padding(
                start = 16.dp,
                top = 8.dp
            ),
            fontWeight = FontWeight.SemiBold,
            fontSize = 30.sp,
        )

        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Search(
                modifier = Modifier.weight(1f),
                onSearch = onSearch
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun SearchBarPreview() {
    StoreAppTheme {
        SearchBar()
    }
}
