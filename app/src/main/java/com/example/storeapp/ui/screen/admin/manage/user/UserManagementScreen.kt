package com.example.storeapp.ui.screen.admin.manage.user

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.storeapp.R
import com.example.storeapp.data.local.DataDummy
import com.example.storeapp.model.Role
import com.example.storeapp.model.UserModel
import com.example.storeapp.ui.AppViewModelProvider
import com.example.storeapp.ui.component.admin.AdminSearch
import com.example.storeapp.ui.component.admin.AdminTopAppBar
import com.example.storeapp.ui.component.admin.FilterList
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.theme.StoreAppTheme

object UserManagementDestination : NavigationDestination {
    override val route = "user_management"
    override val titleRes = R.string.management_user
}

@Composable
fun UserManagementScreen(
    navController: NavController,
    onNavigateProductDetail: (String) -> Unit,
    viewModel: UserManagementViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadData()
    }
    Scaffold(
        topBar = {
            AdminTopAppBar(
                R.drawable.arrowback,
                "Quản lý",
                "Tài khoản",
                { navController.navigateUp() },
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 16.dp)
            )
        },
    ) { innerPadding ->
        UserManagementContent(
            innerPadding = innerPadding,
            uiState = uiState,
            onFilterSelected = { viewModel.selectRole(it) },
            onUserItemClick = {
                onNavigateProductDetail(it.id)
            },
            onSearchUser = { viewModel.searchUsersByName(it) }
        )
    }
}

@Composable
fun UserManagementContent(
    innerPadding: PaddingValues,
    uiState: UserManagementUiState,
    onFilterSelected: (String) -> Unit = {},
    onUserItemClick: (UserModel) -> Unit = {},
    onSearchUser: (String) -> Unit = {}
) {

    val filterList = Role.entries.map { it.toString() }
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
                textSearch = "Tìm kiếm tài khoản",
                onSearch = onSearchUser,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
            FilterList(filterList = filterList, onFilterSelected = onFilterSelected)
            UserManagementList(
                userList = if (uiState.currentQuery.isNotBlank()) {
                    uiState.usersSearched
                } else {
                    uiState.currentListUser
                },
                userItemClick = { onUserItemClick(it) })
        }
    }
}

@Composable
fun UserManagementList(
    userList: List<UserModel>,
    userItemClick: (UserModel) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(userList) { item ->
            UserManagementItem(item = item, userItemClick = { userItemClick(item) })
        }
    }
}

@Composable
fun UserManagementItem(
    item: UserModel,
    userItemClick: () -> Unit = {}
) {
    val name = "${item.firstName} ${item.lastName}"

    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = Modifier.clickable {
            userItemClick()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = (item.imageUrl), contentDescription = "",
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
                            text = name,
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
                        text = "Số điện thoại:",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    )
                    Text(
                        text = item.phone,
                        color = Color.Gray,
                        fontSize = 16.sp,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Email:",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    )
                    Spacer(modifier =Modifier.width(8.dp))
                    Text(
                        text = item.email,
                        color = Color.Gray,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

    }
}

@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewUserManagementContent() {
    StoreAppTheme {
        UserManagementContent(
            uiState = UserManagementUiState(
                listUser = DataDummy.dummyUsers
            ),
            innerPadding = PaddingValues(0.dp)
        )
    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewUserManagementList() {
    StoreAppTheme {
        UserManagementList(DataDummy.dummyUsers)
    }
}

@Preview("Light Theme", showBackground = true)
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewUserManagementItem() {
    StoreAppTheme {
        UserManagementItem(DataDummy.user)
    }
}
