package com.example.storeapp.ui

import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import com.example.storeapp.StoreAppManagerApplication
import com.example.storeapp.ui.ourproduct.OurProductViewModel
import com.example.storeapp.ui.screen.home.HomeViewModel
import com.example.storeapp.ui.screen.productdetails.ProductDetailsViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                storeAppManagerApplication().container.storeAppRepository
            )
        }

        initializer {
            ProductDetailsViewModel(
                this.createSavedStateHandle(),
                storeAppManagerApplication().container.storeAppRepository
            )

        }

        initializer {
            OurProductViewModel(
                storeAppManagerApplication().container.storeAppRepository
            )
        }
    }

}

fun CreationExtras.storeAppManagerApplication(): StoreAppManagerApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as? StoreAppManagerApplication)
        ?: throw IllegalStateException("Application not initialized correctly.")
