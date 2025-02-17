package com.example.storeapp.ui

import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import com.example.storeapp.StoreAppManagerApplication
import com.example.storeapp.ui.screen.cart.CartViewModel
import com.example.storeapp.ui.screen.checkout.CheckoutViewModel
import com.example.storeapp.ui.screen.favorite.WishListViewModel
import com.example.storeapp.ui.screen.ourproduct.OurProductViewModel
import com.example.storeapp.ui.screen.home.HomeViewModel
import com.example.storeapp.ui.screen.login.LoginViewModel
import com.example.storeapp.ui.screen.login.signup.SignUpViewModel
import com.example.storeapp.ui.screen.productdetails.ProductDetailsViewModel

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


    }

}

fun CreationExtras.storeAppManagerApplication(): StoreAppManagerApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as? StoreAppManagerApplication)
        ?: throw IllegalStateException("Application not initialized correctly.")
