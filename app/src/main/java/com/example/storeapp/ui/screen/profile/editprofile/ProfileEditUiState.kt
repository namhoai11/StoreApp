package com.example.storeapp.ui.screen.profile.editprofile

import com.example.storeapp.model.Gender
import com.google.firebase.Timestamp


data class ProfileEditUiState(
    val firstName: String = "",
    val lastName: String = "",
    val dateOfBirth: Timestamp = Timestamp.now(),
    val gender: Gender = Gender.OTHER,
//    val email: String = "",
    val phone: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)