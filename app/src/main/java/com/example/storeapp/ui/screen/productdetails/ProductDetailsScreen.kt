package com.example.storeapp.ui.screen.productdetails

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.storeapp.R
import com.example.storeapp.model.ProductModel
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.user.LoadingBox
import com.example.storeapp.ui.navigation.NavigationDestination
import com.google.firebase.Timestamp


object ProductDetailsDestination : NavigationDestination {
    override val route = "product_details"
    override val titleRes = R.string.productdetails_title
    const val productDetailsIdArg = "productId"
    val routeWithArgs = "$route/{$productDetailsIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    navController: NavController,
    onAddToCartClick: () -> Unit,
    onCartClick: () -> Unit,
    viewModel: ProductDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val productDetailsUiState by viewModel.uiState.collectAsState()
//    var selectedImageUI by remember { mutableStateOf(productDetailsUiState.productDetailsItem.picUrl.firstOrNull()) }
//    Log.d("ProductDetailsContent","selectdImageUI: $selectedImageUI")
//
//
//    LaunchedEffect(productDetailsUiState.productDetailsItem) {
//        selectedImageUI = productDetailsUiState.productDetailsItem.picUrl.firstOrNull() ?: ""
//    }

    var isProductFavorited by remember {
        mutableStateOf(true)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(horizontal = 16.dp),
                title = {
                    Text(
                        text = stringResource(R.string.product_detail),
//                        fontFamily = poppinsFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back",
                        modifier = Modifier.clickable { navController.navigateUp() }
                    )
                },
                actions = {
                    Icon(
                        painter = painterResource(
                            id = if (isProductFavorited) R.drawable.icon_favourite_filled_red
                            else R.drawable.icon_favourite_outlined
                        ),
                        tint = Color.Unspecified,
                        contentDescription = "favourite",
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        if (productDetailsUiState.showProductDetailsLoading) {
            LoadingBox(height = 200.dp)
        }
        else {
            ProductDetailsContent(
                innerPadding = innerPadding,
                item = productDetailsUiState.productDetailsItem
            )
        }
    }
}



@Composable
fun ProductDetailsContent(
    item: ProductModel,
//    modifier: Modifier,
    onAddToCartClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
    innerPadding: PaddingValues
) {
    var selectedImageUrl by remember { mutableStateOf(item.images.firstOrNull()) }
    LaunchedEffect(item.images) {
        selectedImageUrl = item.images.firstOrNull() ?: ""
    }
    Log.d("ProductDetailsContent", "selectdImageUrl: $selectedImageUrl")
    var selectedModelIndex by remember { mutableStateOf(-1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        if (item.images.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(model = selectedImageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(290.dp)
                    .background(
                        colorResource(id = R.color.lightGrey),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            )
        } else {
            Text(
                text = "No image available",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(290.dp)
                    .background(
                        colorResource(id = R.color.lightGrey),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
        }

        LazyRow(modifier = Modifier.padding(vertical = 16.dp)) {
            items(item.images) { imgUrl ->
                ImageThumbnail(
                    imageUrl = imgUrl,
                    isSelected = selectedImageUrl == imgUrl,
                    onClick = {
                        selectedImageUrl = imgUrl
                    }
                )
            }

        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                text = item.name,
                fontSize = 23.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(end = 16.dp)
            )
            Text(
                text = "$${item.price}",
                fontSize = 22.sp
            )
        }
        RatingBar(rating = item.rating)
        ModelSelector(
            models = item.options,
            selectedModelIndex = selectedModelIndex,
            onModelSelected = { selectedModelIndex = it }
        )
        Text(
            text = item.description,
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onAddToCartClick,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.purple)),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                    .height(50.dp)
            ) {
                Text(text = "Buy Now", fontSize = 18.sp)
            }
            IconButton(
                onClick = onCartClick,
                modifier = Modifier.background(
                    colorResource(id = R.color.lightGrey),
                    shape = RoundedCornerShape(10.dp)
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.btn_2),
                    contentDescription = "Cart",
                    tint = Color.Black
                )

            }

        }

    }
}

@Composable
fun RatingBar(rating: Double) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text(
            text = "Select Model",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Image(
            painter = painterResource(id = R.drawable.star),
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = "$rating Rating",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ModelSelector(
    models: List<String>,
    selectedModelIndex: Int,
    onModelSelected: (Int) -> Unit
) {
    LazyRow(modifier = Modifier.padding(vertical = 8.dp)) {
        itemsIndexed(models) { index, model ->
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .height(48.dp)
                    .then(
                        if (index == selectedModelIndex) {
                            Modifier.border(
                                1.dp, colorResource(id = R.color.purple),
                                RoundedCornerShape(10.dp)
                            )
                        } else {
                            Modifier
                        }
                    )
                    .background(
                        if (index == selectedModelIndex) colorResource(id = R.color.lightPurple) else colorResource(
                            id = R.color.lightGrey
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable { onModelSelected(index) }
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = model,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = if (index == selectedModelIndex) colorResource(id = R.color.purple)
                    else colorResource(id = R.color.black),
                    modifier = Modifier.align(Alignment.Center)
                )

            }
        }
    }
}

@Composable
fun ImageThumbnail(
    imageUrl: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backColor =
        if (isSelected) colorResource(id = R.color.lightPurple) else colorResource(id = R.color.lightGrey)
    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(55.dp)
            .then(
                if (isSelected) {
                    Modifier.border(
                        1.dp,
                        colorResource(id = R.color.purple),
                        RoundedCornerShape(10.dp)
                    )
                } else {
                    Modifier
                }
            )
            .background(backColor, shape = RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = imageUrl),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        )
    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PrevewProductDetailsContent() {
    val item = ProductModel(
        id = "1",
        name = "Business Laptop",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed  do eiusmod tempor incididunt ut labore et dolore magna  aliqua. Ut enim ad minim veniam, quis nostrud exercitation  ullamco laboris nisi ut aliquip ex ea commodo consequat.  Duis aute irure dolor in reprehenderit in voluptate velit esse  cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat  cupidatat non proident, sunt in culpa qui officia deserunt .Excepteur sint occaecat",
        images = arrayListOf(
            "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_1.png?alt=media&token=fb49a7c9-3094-4f5c-9ea6-b8365cd86323",
            "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_2.png?alt=media&token=3f826014-4808-4387-af6f-22dc7ddd4780",
            "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_3.png?alt=media&token=d4ab793a-cb72-45ab-ae43-8db69adaaeba",
            "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_4.png?alt=media&token=dfb10462-9138-471a-b34a-537bc7f5b7c8",
            "https://firebasestorage.googleapis.com/v0/b/project-200-1.appspot.com/o/cat2_5.png?alt=media&token=2bfd17ef-d8c5-409e-8d6c-2d9e57d394c4"
        ),
        options = arrayListOf(
            "core i3",
            "core i5",
            "core i7"
        ),
        price = 550.0,
        rating = 4.7,
        categoryId = "0",
        hidden = false,
        stockQuantity = 12,
        createdAt = Timestamp.now(),
        updatedAt = Timestamp.now(),
    )
    ProductDetailsContent(item = item, innerPadding = PaddingValues(0.dp))
}