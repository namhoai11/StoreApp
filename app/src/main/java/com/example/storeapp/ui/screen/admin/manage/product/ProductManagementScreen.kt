package com.example.storeapp.ui.screen.admin.manage.product

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.storeapp.R
import com.example.storeapp.data.local.DataDummy
import com.example.storeapp.model.ProductModel
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.admin.AdminSearch
import com.example.storeapp.ui.component.admin.AdminTopAppBar
import com.example.storeapp.ui.component.admin.FilterList
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.theme.StoreAppTheme


object ProductManagementDestination : NavigationDestination {
    override val route = "product management"
    override val titleRes = R.string.management_product
}

@Composable
fun ProductManagementScreen(
    navController: NavController,
    onAddProductClick: () -> Unit,
    onNavigateProductDetail: (String) -> Unit,
    viewModel: ProductManagementViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadData()
    }
    LaunchedEffect(uiState.categories) {
        if (uiState.categories.isNotEmpty() && uiState.currentCategoryId == "-2") {
            viewModel.selectCategory(uiState.categories.first().name)
        }
    }


    Scaffold(
        topBar = {
            AdminTopAppBar(
                R.drawable.arrowback,
                "Quản lý",
                "Sản phẩm",
                { navController.navigateUp() },
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 16.dp)
            )
        },
    ) { innerPadding ->
        ProductManagementContent(
            innerPadding = innerPadding,
            uiState = uiState,
            onAddProductClick = onAddProductClick,
            onFilterSelected = { viewModel.selectCategory(it) },
            onProductItemClick = {
                onNavigateProductDetail(it.id)
            },
            onSearchProduct = {viewModel.searchProductsByName(it)}
        )
    }
}

@Composable
fun ProductManagementContent(
    innerPadding: PaddingValues,
    uiState: ProductManagementUiState,
    onAddProductClick: () -> Unit = {},
    onFilterSelected: (String) -> Unit = {},
    onProductItemClick: (ProductModel) -> Unit = {},
    onSearchProduct: (String) -> Unit = {}
) {

    val listCategory = uiState.categories.map { it.name }
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()

    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            AdminSearch(
                textSearch = "Tìm kiếm sản phẩm",
                onSearch = onSearchProduct,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
            FilterList(filterList = listCategory, onFilterSelected = onFilterSelected)
            ProductManagementList(
                productList = if (uiState.currentQuery.isNotBlank()) {
                    uiState.productsSearched
                } else {
                    uiState.currentListItems
                },
                productItemClick = { onProductItemClick(it) })

        }
        Card(
            border = BorderStroke(
                width = 1.dp,
                color = Color.LightGray
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 24.dp, horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onAddProductClick()
                    }
            ) {
                Text(
                    text = "Thêm sản phẩm",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun ProductManagementList(
    productList: List<ProductModel>,
    productItemClick: (ProductModel) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(productList) { item ->
            ProductManagementItem(item = item, productItemClick = { productItemClick(item) })
        }
    }
}

@Composable
fun ProductManagementItem(
    item: ProductModel,
    productItemClick: () -> Unit = {}
) {
    val stockVar = item.stockByVariant.sumOf { it.quantity }
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = Modifier.clickable {
            productItemClick()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = (item.images.firstOrNull()), contentDescription = item.name,
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
                            text = item.id,
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                }
                HorizontalDivider(
                    thickness = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Giá:",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    )
                    Text(
                        text = "${item.price}",
                        color = Color.Gray,
                        fontSize = 16.sp,
                    )

                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Tồn kho:",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    )
                    Text(
                        text = "$stockVar",
                        color = Color.Gray,
                        fontSize = 16.sp,
                    )
                }
            }
        }

    }
}

@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewProductManagementContent() {
    StoreAppTheme {
        ProductManagementContent(
            uiState = DataDummy.productManagementUiState,
            innerPadding = PaddingValues(0.dp)
        )
    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewProductManagementList() {
    val sampleCategories = DataDummy.listProduct
    StoreAppTheme {
        ProductManagementList(sampleCategories)
    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewProductManagementItem() {
    val sampleItem = DataDummy.productItem
    StoreAppTheme {
        ProductManagementItem(sampleItem)
    }
}

