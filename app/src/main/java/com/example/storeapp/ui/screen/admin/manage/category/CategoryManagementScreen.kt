package com.example.storeapp.ui.screen.admin.manage.category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.storeapp.R
import com.example.storeapp.data.local.DataDummy
import com.example.storeapp.model.CategoryModel
import com.example.storeapp.ui.component.admin.AdminSearch
import com.example.storeapp.ui.component.admin.AdminTopAppBar
import com.example.storeapp.ui.component.admin.CategoryManagementList
import com.example.storeapp.ui.navigation.NavigationDestination
import com.example.storeapp.ui.theme.StoreAppTheme
import com.google.firebase.Timestamp

object CategoryManagementDestination : NavigationDestination {
    override val route = "categorymanagement"
    override val titleRes = R.string.categorymanage_title
}

@Composable
fun CategoryManagementScreen(
    navController: NavController,
) {
    Scaffold(
        topBar = {
            AdminTopAppBar(
                R.drawable.arrowback,
                "Quản lý",
                "Danh mục",
                { navController.navigateUp() },
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 16.dp)
            )
        },
//        bottomBar = {
//            AdminBottomNavigationBar(
//                navController = navController,
//                currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
//
//            )
//        }
    ) { innerPadding ->
        CategoryManagementContent(innerPadding = innerPadding)
    }
}

@Composable
fun CategoryManagementContent(
    innerPadding: PaddingValues,
) {
    val sampleCategories = DataDummy.categoryList
    Column(
        modifier = Modifier.padding(innerPadding)
    ) {
        AdminSearch(
            textSearch = "Tìm kiếm danh mục",
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )
        CategoryManagementList(categoryList = sampleCategories)
    }
}

@Preview("Light Theme", showBackground = true)
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewCategoryManagementScreen() {
    StoreAppTheme {
        CategoryManagementScreen(
            navController = rememberNavController()
        )
    }
}