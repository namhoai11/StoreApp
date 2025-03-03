package com.example.storeapp.ui.screen.profile

import androidx.lifecycle.ViewModel
import com.example.storeapp.data.repository.FirebaseAuthRepository

class ProfileViewModel (
    private val auth:FirebaseAuthRepository
) : ViewModel() {

    fun logOut(){
        auth.logoutUser()
    }
}