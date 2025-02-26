package com.example.storeapp.ui.screen.address

import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.user.AddressItemScreen2
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.theme.StoreAppTheme


object AddressDestination : NavigationDestination {
    override val route = "address"
    override val titleRes = R.string.address_title
    const val addressSetupRole = "addressSetupRole"
    val routeWithSetupRole = "$route/{$addressSetupRole}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(
    navController: NavController,
    onNavigateToAddAddress: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onReturnToCheckout:(String)->Unit,
    viewModel: AddressViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(Unit) {
        viewModel.loadAddress()
    }
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Địa chỉ",
//                        fontFamily = poppinsFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back",
                        modifier = Modifier
                            .clickable { navController.navigateUp() }
                            .padding(horizontal = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        AddressContent(
            uiState = uiState,
            innerPadding = innerPadding,
//            state = DataDummy.dummyUserLocation,
//            selectedItemId = "2",
            onSelectedItem = { viewModel.onAddressItemSelected(it) },
            onDeletedItem = {viewModel.confirmDeleteLocationClicked(it)},
            onConfirmationClick = {
                viewModel.onSetDefaultLocationClicked()
                onNavigateToProfile()
            },
            onConfirmationForCheckoutClick = {onReturnToCheckout(it)},
            onAddNewAddressClick = { onNavigateToAddAddress() },
        )

    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddressContent(
    modifier: Modifier = Modifier,
    uiState: AddressUiState,
    innerPadding: PaddingValues,
//    state: List<UserLocationModel>,
//    selectedItemId: String?,
    onSelectedItem: (String) -> Unit,
    onDeletedItem: (String) -> Unit,
    onConfirmationClick: () -> Unit,
    onConfirmationForCheckoutClick:(String)->Unit,
    onAddNewAddressClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(innerPadding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            if (uiState.addressList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        .fillMaxWidth()
                        .height(200.dp),
                ) {
                    Text(
                        text = "No Address Found",
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            } else {
                LazyColumn {
                    items(items = uiState.addressList, key = { it.id }) { userLocation ->
                        val address =
                            userLocation.street + " - " + userLocation.ward + " - " + userLocation.district + " - " + userLocation.province
                        AddressItemScreen2(
                            name = userLocation.userName,
                            address = address,
                            isSelected = uiState.selectedItemId == userLocation.id,
                            onChooseClick = { onSelectedItem(userLocation.id) },
                            onDeleteClick = { onDeletedItem(userLocation.id) },
                            modifier = Modifier.animateItem(
                                fadeInSpec = null,
                                fadeOutSpec = null,
                                placementSpec = tween(100)
                            )
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)) {
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
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onAddNewAddressClick()
                        }
                ) {
                    Text(
                        text = "Add New Address",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.Center)
                    )
                }
            }

            Spacer(modifier = Modifier.size(16.dp))
            if(uiState.isDefaultLocationSetting){
                Button(
                    enabled = uiState.selectedItemId != null,
                    modifier = Modifier
                        .height(55.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    onClick = onConfirmationClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Xác nhận địa chỉ mặc định",
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }else{
                Button(
                    enabled = uiState.selectedItemId != null,
                    modifier = Modifier
                        .height(55.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    onClick = { uiState.selectedItemId?.let { onConfirmationForCheckoutClick(it) } },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "chọn địa chỉ",
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
private fun AddressContentPreview() {
    StoreAppTheme(dynamicColor = false) {
        AddressContent(
            uiState = DataDummy.addressUiState,
            innerPadding = PaddingValues(0.dp),
//            state = DataDummy.dummyUserLocation,
//            scope = rememberCoroutineScope(),
//            selectedItemId = "2",
            onSelectedItem = {},
            onDeletedItem = {},
            onConfirmationClick = {},
            onConfirmationForCheckoutClick = {},
            onAddNewAddressClick = {},
        )
    }
}