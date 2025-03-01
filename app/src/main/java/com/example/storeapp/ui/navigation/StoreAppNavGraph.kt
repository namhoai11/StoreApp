package com.example.storeapp.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.storeapp.ui.screen.address.AddressDestination
import com.example.storeapp.ui.screen.address.AddressScreen
import com.example.storeapp.ui.screen.address.add_address.AddAddressDestination
import com.example.storeapp.ui.screen.address.add_address.AddAddressScreen
import com.example.storeapp.ui.screen.cart.CartDestination
import com.example.storeapp.ui.screen.cart.CartScreen
import com.example.storeapp.ui.screen.admin.dashboard.DashBoardScreen
import com.example.storeapp.ui.screen.admin.dashboard.DashboardAdminDestination
import com.example.storeapp.ui.screen.admin.manage.ManageScreen
import com.example.storeapp.ui.screen.admin.manage.ManegeAdminDestination
import com.example.storeapp.ui.screen.admin.manage.category.CategoryManagementDestination
import com.example.storeapp.ui.screen.admin.manage.category.CategoryManagementScreen
import com.example.storeapp.ui.screen.admin.manage.category.add_category.AddCategoryManagementDestination
import com.example.storeapp.ui.screen.admin.manage.category.add_category.AddCategoryScreen
import com.example.storeapp.ui.screen.admin.manage.coupon.CouponManagementDestination
import com.example.storeapp.ui.screen.admin.manage.coupon.CouponManagementScreen
import com.example.storeapp.ui.screen.admin.manage.coupon.add_coupon.AddCouponManagementDestination
import com.example.storeapp.ui.screen.admin.manage.coupon.add_coupon.AddCouponScreen
import com.example.storeapp.ui.screen.admin.manage.orders.OrderManagementDestination
import com.example.storeapp.ui.screen.admin.manage.orders.OrderManagementScreen
import com.example.storeapp.ui.screen.admin.manage.orders.orderdetailsmanagement.OrderDetailsManagementDestination
import com.example.storeapp.ui.screen.admin.manage.orders.orderdetailsmanagement.OrderDetailsManagementScreen
import com.example.storeapp.ui.screen.admin.manage.product.ProductManagementDestination
import com.example.storeapp.ui.screen.admin.manage.product.ProductManagementScreen
import com.example.storeapp.ui.screen.admin.manage.product.add_product.AddProductManagementDestination
import com.example.storeapp.ui.screen.admin.manage.product.add_product.AddProductScreen
import com.example.storeapp.ui.screen.favorite.FavoriteDestination
import com.example.storeapp.ui.screen.favorite.WishListScreen
import com.example.storeapp.ui.screen.ourproduct.OurProductDestination
import com.example.storeapp.ui.screen.ourproduct.OurProductScreen
import com.example.storeapp.ui.screen.category.CategoryDestination
import com.example.storeapp.ui.screen.category.CategoryScreen
import com.example.storeapp.ui.screen.checkout.CheckoutDestination
import com.example.storeapp.ui.screen.checkout.CheckoutScreen
import com.example.storeapp.ui.screen.checkout.payment.PaymentDestination
import com.example.storeapp.ui.screen.checkout.payment.PaymentScreen
import com.example.storeapp.ui.screen.checkout.successpayment.SuccessPaymentDestination
import com.example.storeapp.ui.screen.checkout.successpayment.SuccessPaymentScreen
import com.example.storeapp.ui.screen.home.HomeDestination
import com.example.storeapp.ui.screen.home.HomeScreen
import com.example.storeapp.ui.screen.intro.IntroDestination
import com.example.storeapp.ui.screen.intro.IntroScreen
import com.example.storeapp.ui.screen.login.LoginDestination
import com.example.storeapp.ui.screen.login.LoginScreen
import com.example.storeapp.ui.screen.login.forgotpassword.ForgotPasswordDestination
import com.example.storeapp.ui.screen.login.forgotpassword.ForgotPasswordScreen
import com.example.storeapp.ui.screen.notification.NotificateDestination
import com.example.storeapp.ui.screen.notification.NotificationScreen
import com.example.storeapp.ui.screen.order.OrdersDestination
import com.example.storeapp.ui.screen.order.OrdersScreen
import com.example.storeapp.ui.screen.productdetails.ProductDetailsDestination
import com.example.storeapp.ui.screen.productdetails.ProductDetailsScreen
import com.example.storeapp.ui.screen.profile.ProfileDestination
import com.example.storeapp.ui.screen.profile.ProfileScreen
import com.example.storeapp.ui.screen.login.signup.SignUpDestination
import com.example.storeapp.ui.screen.login.signup.SignUpScreen
import com.example.storeapp.ui.screen.login.verify.DoneVerifyDestination
import com.example.storeapp.ui.screen.login.verify.DoneVerifyScreen
import com.example.storeapp.ui.screen.login.verify.VerifyAccountDestination
import com.example.storeapp.ui.screen.login.verify.VerifyScreen
import com.example.storeapp.ui.screen.order.orderdetails.OrderDetailsDestination
import com.example.storeapp.ui.screen.order.orderdetails.OrderDetailsScreen

@Composable
fun StoreAppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = IntroDestination.route,
        modifier = modifier
    ) {
        composable(route = IntroDestination.route) {
            IntroScreen(
                onNavigateSignIn = { navController.navigate(LoginDestination.route) },
                onNavigateHome = {
                    Log.d("NavStack", "Navigating to ${HomeDestination.route}")
                    navController.navigate(HomeDestination.route) {
                        popUpTo(IntroDestination.route) { inclusive = true }
                    }
                },
                onNavigateSignUp = {
                    navController.navigate(SignUpDestination.route)
                }
            )
        }
        composable(route = LoginDestination.route) {
            LoginScreen(
                onNavigateSignUp = {
                    navController.navigate(SignUpDestination.route)
                },
                onNavigateForgotPassword = {
                    navController.navigate(ForgotPasswordDestination.route)
                },
                onNavigateHome = {
                    navController.navigate(HomeDestination.route)
                }
            )
        }
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateCartScreen = { navController.navigate(CartDestination.route) },
                navigateAllProduct = { navController.navigate(CategoryDestination.route) },
                navigateProductDetails = {
                    navController.navigate("${ProductDetailsDestination.route}/$it")
                },
                navigateNotification = { navController.navigate(NotificateDestination.route) },
                navController = navController
            )
        }
        composable(route = CategoryDestination.route) {
            CategoryScreen(
                navController = navController,
                navigateProductDetails = {
                    navController.navigate("${ProductDetailsDestination.route}/$it")
                }
            )
        }
        composable(
            route = ProductDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ProductDetailsDestination.productDetailsIdArg) {
                type = NavType.StringType
            })
        ) {
            ProductDetailsScreen(
                navController = navController,
                onNavigateToCart = { navController.navigate(CartDestination.route) }
            )
        }
        composable(
            route = OurProductDestination.route
        ) {
            OurProductScreen(
                navigateProductDetails = {
                    navController.navigate("${ProductDetailsDestination.route}/$it")
                },
                navController = navController
            )
        }
        composable(
            CartDestination.route
        ) {
            CartScreen(navController = navController,
                onNavigateProductDetails = { navController.navigate("${ProductDetailsDestination.route}/$it") },
                onNavigateToCheckout = {
                    navController.navigate(
                        CheckoutDestination.createRoute(locationId = null)
                    )
                }
            )
        }

        composable(
            CheckoutDestination.route,
            arguments = listOf(
                navArgument("locationId") { type = NavType.StringType; nullable = true },
            )
        ) {
            CheckoutScreen(navController = navController,
                onNavigateChooseAddress = {
                    navController.navigate(
                        AddressDestination.routeWithSetupRole.replace(
                            "{addressSetupRole}",
                            "1"
                        )
                    )
                },
                onNavigateToPayment = {
                    navController.navigate(
                        PaymentDestination.createRoute(
                            orderId = it
                        )
                    )
                }
            )
        }

        composable(
            PaymentDestination.route,
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType; nullable = true },
            )
        ) {
            PaymentScreen(
                navController = navController,
                onNavigateToSuccessPayment = {
                    navController.navigate(
                        SuccessPaymentDestination.createRoute(
                            orderId = it
                        )
                    )
                })

        }
        composable(
            SuccessPaymentDestination.route,
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType; nullable = true },
            )
        ) {
            SuccessPaymentScreen(onNavigateHome = { navController.navigate(HomeDestination.route) })

        }
        composable(
            AddressDestination.routeWithSetupRole,
            arguments = listOf(navArgument(AddressDestination.addressSetupRole) {
                type = NavType.IntType
            })
        ) {
            AddressScreen(navController = navController,
                onNavigateToAddAddress = {
                    navController.navigate(AddAddressDestination.route)
                },
                onNavigateToProfile = {
                    navController.navigate(ProfileDestination.route)
                },
                onReturnToCheckout = {
                    navController.navigate(
                        CheckoutDestination.createRoute(locationId = it)
                    )
                }
            )
        }
        composable(AddAddressDestination.route) {
            AddAddressScreen(navController = navController)
        }
        composable(
            FavoriteDestination.route
        ) {
            WishListScreen(
                navController = navController,
                navigateProductDetails = {
                    navController.navigate("${ProductDetailsDestination.route}/$it")
                }
            )
        }

        composable(
            NotificateDestination.route
        ) {
            NotificationScreen(navController = navController)
        }

        composable(OrdersDestination.route) {
            OrdersScreen(navController = navController,
                onNavigateOrderDetails = {
                    navController.navigate(
                        OrderDetailsDestination.createRoute(
                            orderId = it
                        )
                    )
                }
            )
        }
        composable(
            OrderDetailsDestination.route,
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType; nullable = true },
            )
        ) {
            OrderDetailsScreen(navController = navController,
                onNavigateToPayment = {
                    navController.navigate(
                        PaymentDestination.createRoute(
                            orderId = it
                        )
                    )
                },
                navigateToOrderScreen = { navController.navigate(OrdersDestination.route) }
            )
        }
        composable(ProfileDestination.route) {
            ProfileScreen(navController)
        }
        composable(DashboardAdminDestination.route) {
            DashBoardScreen(navController = navController,
                navigateUserApp = {
                    navController.navigate(ProfileDestination.route)
                })
        }
        composable(ManegeAdminDestination.route) {
            ManageScreen(navController = navController,
                navigateUserApp = {
                    navController.navigate(ProfileDestination.route)
                })
        }


        composable(OrderManagementDestination.route) {
            OrderManagementScreen(navController = navController,
                onNavigateOrderManagementDetail = {
                    navController.navigate(OrderDetailsManagementDestination.createRoute(it))
                }
            )
        }
        composable(
            route = OrderDetailsManagementDestination.route,
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType; nullable = true },
            )
        ) {
            OrderDetailsManagementScreen(navController = navController)
        }

        composable(CouponManagementDestination.route) {
            CouponManagementScreen(navController = navController,
                onAddCouponClick = {
                    navController.navigate(AddCouponManagementDestination.createRoute(null, true))
                },
                onNavigateCouponDetail = {
                    navController.navigate(AddCouponManagementDestination.createRoute(it, false))
                }
            )
        }

        composable(
            route = AddCouponManagementDestination.route,
            arguments = listOf(
                navArgument("couponId") { type = NavType.StringType; nullable = true },
                navArgument("isEditing") { type = NavType.BoolType; defaultValue = false }
            )
        ) {
            AddCouponScreen(navController = navController)
        }

        composable(ProductManagementDestination.route) {
            ProductManagementScreen(
                navController = navController,
                onAddProductClick = {
                    navController.navigate(AddProductManagementDestination.createRoute(null, true))

                },
                onNavigateProductDetail = {
                    navController.navigate(AddProductManagementDestination.createRoute(it, false))
                })
        }

        composable(
            route = AddProductManagementDestination.route,
            arguments = listOf(
                navArgument("productId") { type = NavType.StringType; nullable = true },
                navArgument("isEditing") { type = NavType.BoolType; defaultValue = false }
            )
        ) {
            AddProductScreen(navController = navController)
        }


        composable(CategoryManagementDestination.route) {
            CategoryManagementScreen(
                navController = navController,
                onAddCategoryClick = {
                    navController.navigate(
                        AddCategoryManagementDestination.createRoute(
                            null,
                            true
                        )
                    )
                },
                onNavigateCategoryDetail = {
                    navController.navigate(AddCategoryManagementDestination.createRoute(it, false))
                }
            )
        }
        composable(
            route = AddCategoryManagementDestination.route,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType; nullable = true },
                navArgument("isEditing") { type = NavType.BoolType; defaultValue = false }
            )
        ) {
            AddCategoryScreen(navController = navController)
        }

        composable(SignUpDestination.route) {
            SignUpScreen(
                onNavigateSignIn = {
                    navController.navigate(LoginDestination.route)
                }
            )
        }
        composable(ForgotPasswordDestination.route) {
            ForgotPasswordScreen(
                navController = navController,
                onNavigateSignIn = {
                    navController.navigate(LoginDestination.route)
                },
                onNavigateVerify = {
                    navController.navigate(VerifyAccountDestination.route)
                })
        }
        composable(VerifyAccountDestination.route) {
            VerifyScreen(navController = navController,
                onNavigateDoneVerify = {
                    navController.navigate(DoneVerifyDestination.route)
                })
        }
        composable(DoneVerifyDestination.route) {
            DoneVerifyScreen()
        }
    }
}