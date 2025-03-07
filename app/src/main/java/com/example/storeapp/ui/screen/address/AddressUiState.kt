﻿package com.example.storeapp.ui.screen.address

import com.example.storeapp.model.UserLocationModel

data class AddressUiState(
    val addressList: List<UserLocationModel> = emptyList(),
    val selectedItemId: String? = null,
    val isDefaultLocationSetting: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)
