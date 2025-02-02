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
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 48.dp)
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