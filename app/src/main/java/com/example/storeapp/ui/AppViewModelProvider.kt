package com.example.storeapp.ui

import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import com.example.storeapp.StoreAppManagerApplication
import com.example.storeapp.ui.screen.address.AddressViewModel
import com.example.storeapp.ui.screen.address.add_address.AddAddressViewModel
import com.example.storeapp.ui.screen.admin.dashboard.DashBoardViewModel
import com.example.storeapp.ui.screen.admin.manage.category.CategoryManagementViewModel
import com.example.storeapp.ui.screen.admin.manage.category.add_category.AddCategoryViewModel
import com.example.storeapp.ui.screen.admin.manage.coupon.CouponManagementViewModel
import com.example.storeapp.ui.screen.admin.manage.coupon.add_coupon.AddCouponViewModel
import com.example.storeapp.ui.screen.admin.manage.orders.OrderManagementViewModel
import com.example.storeapp.ui.screen.admin.manage.orders.orderdetailsmanagement.OrderDetailsManagementViewModel
import com.example.storeapp.ui.screen.admin.manage.product.ProductManagementViewModel
import com.example.storeapp.ui.screen.admin.manage.product.add_product.AddProductViewModel
import com.example.storeapp.ui.screen.cart.CartViewModel
import com.example.storeapp.ui.screen.checkout.CheckoutViewModel
import com.example.storeapp.ui.screen.checkout.payment.PaymentViewModel
import com.example.storeapp.ui.screen.checkout.successpayment.SuccessPaymentViewModel
import com.example.storeapp.ui.screen.favorite.WishListViewModel
import com.example.storeapp.ui.screen.ourproduct.OurProductViewModel
import com.example.storeapp.ui.screen.home.HomeViewModel
import com.example.storeapp.ui.screen.login.LoginViewModel
import com.example.storeapp.ui.screen.login.changepassword.ChangePasswordViewModel
import com.example.storeapp.ui.screen.login.forgotpassword.ForgotPasswordViewModel
import com.example.storeapp.ui.screen.login.signup.SignUpViewModel
import com.example.storeapp.ui.screen.order.OrderViewModel
import com.example.storeapp.ui.screen.order.orderdetails.OrderDetailsViewModel
import com.example.storeapp.ui.screen.productdetails.ProductDetailsViewModel
import com.example.storeapp.ui.screen.profile.ProfileViewModel
import com.example.storeapp.ui.screen.profile.editprofile.ProfileEditViewModel
import com.example.storeapp.ui.screen.profile.profiledetails.ProfileDetailViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                storeAppManagerApplication().container.firebaseFireStoreRepository
            )
        }
        initializer {
            ProductDetailsViewModel(
                this.createSavedStateHandle(),
                storeAppManagerApplication().container.firebaseFireStoreRepository,
            )
        }
        initializer {
            CartViewModel(
                storeAppManagerApplication().container.firebaseFireStoreRepository,
            )
        }
        initializer {
            CheckoutViewModel(
                this.createSavedStateHandle(),
                storeAppManagerApplication().container.firebaseFireStoreRepository,
            )
        }
        initializer {
            PaymentViewModel(
                this.createSavedStateHandle(),
                storeAppManagerApplication().container.firebaseFireStoreRepository,
            )
        }
        initializer {
            SuccessPaymentViewModel(
                this.createSavedStateHandle(),
                storeAppManagerApplication().container.firebaseFireStoreRepository,
            )
        }
        initializer {
            OrderViewModel(
                storeAppManagerApplication().container.firebaseFireStoreRepository,
            )
        }

        initializer {
            OrderDetailsViewModel(
                this.createSavedStateHandle(),
                storeAppManagerApplication().container.firebaseFireStoreRepository,
            )
        }

        initializer {
            AddressViewModel(
                this.createSavedStateHandle(),
                storeAppManagerApplication().container.firebaseFireStoreRepository
            )
        }
        initializer {
            AddAddressViewModel(
                storeAppManagerApplication().container.firebaseFireStoreRepository,
                storeAppManagerApplication().container.locationApiService
            )
        }
        initializer {
            ProfileViewModel(
                storeAppManagerApplication().container.firebaseAuthRepository,
            )
        }
        initializer {
            ProfileDetailViewModel(
                storeAppManagerApplication().container.firebaseFireStoreRepository,
            )
        }

        initializer {
            ProfileEditViewModel(
                storeAppManagerApplication().container.firebaseFireStoreRepository,
            )
        }

        initializer {
            DashBoardViewModel(
                storeAppManagerApplication().container.firebaseFireStoreRepository,
            )
        }

        initializer {
            AddCouponViewModel(
                this.createSavedStateHandle(),
                storeAppManagerApplication().container.firebaseFireStoreRepository,
            )
        }

        initializer {
            AddProductViewModel(
                this.createSavedStateHandle(),
                storeAppManagerApplication().container.firebaseFireStoreRepository,
            )
        }

        initializer {
            AddCategoryViewModel(
                this.createSavedStateHandle(),
                storeAppManagerApplication().container.firebaseFireStoreRepository,
            )
        }

        initializer {
            CouponManagementViewModel(
                storeAppManagerApplication().container.firebaseFireStoreRepository,
            )
        }
        initializer {
            ProductManagementViewModel(
                storeAppManagerApplication().container.firebaseFireStoreRepository,
            )
        }
        initializer {
            CategoryManagementViewModel(
                storeAppManagerApplication().container.firebaseFireStoreRepository,
            )
        }
        initializer {
            OrderManagementViewModel(
                storeAppManagerApplication().container.firebaseFireStoreRepository,
            )
        }

        initializer {
            OrderDetailsManagementViewModel(
                this.createSavedStateHandle(),
                storeAppManagerApplication().container.firebaseFireStoreRepository,
            )
        }



        initializer {
            OurProductViewModel(
                storeAppManagerApplication().container.firebaseFireStoreRepository
            )
        }
        initializer {
            WishListViewModel(
                storeAppManagerApplication().container.firebaseFireStoreRepository
            )
        }
        initializer {
            SignUpViewModel(
                storeAppManagerApplication().container.firebaseAuthRepository
            )
        }
        initializer {
            LoginViewModel(
                storeAppManagerApplication().container.firebaseAuthRepository
            )
        }
        initializer {
            ChangePasswordViewModel(
                storeAppManagerApplication().container.firebaseFireStoreRepository,
                storeAppManagerApplication().container.firebaseAuthRepository
            )
        }
        initializer {
            ForgotPasswordViewModel(
                storeAppManagerApplication().container.firebaseAuthRepository
            )
        }
    }
}

fun CreationExtras.storeAppManagerApplication(): StoreAppManagerApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as? StoreAppManagerApplication)
        ?: throw IllegalStateException("Application not initialized correctly.")
