package com.example.storeapp.ui.screen.login.signup

import com.example.storeapp.model.Gender
import com.google.firebase.Timestamp

data class SignUpUiState(
    val firstName: String = "",
    val lastName: String = "",
    val dateOfBirth: Timestamp = Timestamp.now(),
    val gender: Gender = Gender.OTHER,
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isChecked: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)