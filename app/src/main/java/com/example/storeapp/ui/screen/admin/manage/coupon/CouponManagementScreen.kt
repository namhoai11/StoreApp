package com.example.storeapp.ui.screen.admin.manage.coupon

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.storeapp.R
import com.example.storeapp.data.local.DataDummy
import com.example.storeapp.model.CouponActive
import com.example.storeapp.model.CouponModel
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.admin.AdminTopAppBar
import com.example.storeapp.ui.component.admin.CouponManagementList
import com.example.storeapp.ui.component.admin.FilterList
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.screen.admin.manage.coupon.add_coupon.AddCouponViewModel
import com.example.storeapp.ui.theme.StoreAppTheme

object CouponManagementDestination : NavigationDestination {
    override val route = "couponmanagement"
    override val titleRes = R.string.couponmanage_title
}

@Composable
fun CouponManagementScreen(
    navController: NavController,
    onAddCouponClick: () -> Unit = {},
    onNavigateCouponDetail: (String) -> Unit,
    viewModel: CouponManagementViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCoupons()
    }
    Scaffold(
        topBar = {
            AdminTopAppBar(
                R.drawable.arrowback,
                "Quản lý",
                "Khuyến mãi",
                { navController.navigateUp() },
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 16.dp)
            )
        },
    ) { innerPadding ->
        CouponManagementContent(
            innerPadding = innerPadding,
            uiState = uiState,
            onAddCouponClick = onAddCouponClick,
            onFilterSelected = { viewModel.selectActive(it) },
            onCouponItemClick = {
                onNavigateCouponDetail(it.code)
            }
        )

    }
}


@Composable
fun CouponManagementContent(
    innerPadding: PaddingValues,
    uiState: CouponManagementUiState,
    onAddCouponClick: () -> Unit = {},
    onFilterSelected: (String) -> Unit = {},
    onCouponItemClick: (CouponModel) -> Unit = {}
) {
    val filterList = CouponActive.entries.map { it.toString() }
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start=16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            FilterList(filterList = filterList, onFilterSelected = onFilterSelected)
            CouponManagementList(
                listCoupon = uiState.currentListCoupon,
                couponItemClick = { onCouponItemClick(it) }
            )
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
                        onAddCouponClick()
                    }
            ) {
                Text(
                    text = "Thêm khuyến mãi",
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

@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewCouponManagementContent() {
    StoreAppTheme {
        CouponManagementContent(
            innerPadding = PaddingValues(0.dp),
            uiState = DataDummy.couponManagementUiState
        )
    }
}