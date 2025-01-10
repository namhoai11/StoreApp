package com.example.storeapp.ui

import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import com.example.storeapp.StoreAppManagerApplication
import com.example.storeapp.ui.screen.home.HomeViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer { HomeViewModel(storeAppManagerApplication().container.storeAppRepository) }
    }

}

fun CreationExtras.storeAppManagerApplication(): StoreAppManagerApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as? StoreAppManagerApplication)
        ?: throw IllegalStateException("Application not initialized correctly.")
