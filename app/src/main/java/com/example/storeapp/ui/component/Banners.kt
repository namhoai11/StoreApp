package com.example.storeapp.ui.component

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.storeapp.R
import com.example.storeapp.model.SliderModel
import com.example.storeapp.ui.theme.StoreAppTheme


@Composable
fun Banners(banners: List<SliderModel>) {
    AutoSlidingCarousel(banners = banners)
}

@Composable
fun AutoSlidingCarousel(
    modifier: Modifier = Modifier,
    banners: List<SliderModel>,
) {
    val pagerState = rememberPagerState(pageCount = { banners.size })

    Column(modifier = modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .height(150.dp)
        ) { page ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(banners[page].url)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
            )
        }

        DotIndicator(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .align(Alignment.CenterHorizontally),
            totalDot = banners.size,
            selectedIndex = pagerState.currentPage,
            dotSize = 10.dp,
            spacing = 4.dp
        )
    }
}


@Composable
fun DotIndicator(
    modifier: Modifier = Modifier,
    totalDot: Int,
    selectedIndex: Int,
    selectedColor: Color = colorResource(id = R.color.purple),
    unSelectedColor: Color = colorResource(id = R.color.grey),
    dotSize: Dp,
    spacing: Dp = 4.dp,
) {
    LazyRow(
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        items(totalDot) { index ->
            IndicatorDot(
                isSelected = index == selectedIndex,
                size = dotSize,
                selectedColor = selectedColor,
                unSelectedColor = unSelectedColor
            )
        }
    }
}

@Composable
fun IndicatorDot(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    size: Dp,
    selectedColor: Color,
    unSelectedColor: Color
) {
    val animatedColor by animateColorAsState(
        targetValue = if (isSelected) selectedColor else unSelectedColor,
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(animatedColor)
    )
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DotIndicatorPreview() {
    StoreAppTheme {
        DotIndicator(
            totalDot = 3,
            selectedIndex = 0,
            dotSize = 10.dp,
            spacing = 4.dp
        )
    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BannersPreview() {
    StoreAppTheme {
        val banners = listOf(
            SliderModel(url = "https://firebasestorage.googleapis.com/v0/b/project208-2.appspot.com/o/banner1.png?alt=media&token=91f65188-3bff-4ad6-8b02-31c00aeb3ca6"),
            SliderModel(url = "https://firebasestorage.googleapis.com/v0/b/project208-2.appspot.com/o/banner1.png?alt=media&token=91f65188-3bff-4ad6-8b02-31c00aeb3ca6")
        )
        Banners(
            banners
        )
    }
}