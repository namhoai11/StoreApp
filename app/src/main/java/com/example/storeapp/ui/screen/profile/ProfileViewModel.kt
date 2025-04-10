﻿package com.example.storeapp.ui.screen.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.FirebaseAuthRepository
import com.example.storeapp.data.repository.FirebaseFireStoreRepository
import com.example.storeapp.model.UserModel
import kotlinx.coroutines.launch

class ProfileViewModel (
    private val repository: FirebaseFireStoreRepository,
    private val auth:FirebaseAuthRepository
) : ViewModel() {
    private val _user = MutableLiveData<UserModel?>()
    val user: LiveData<UserModel?> = _user

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            val userData = repository.getCurrentUser()
            _user.value = userData
            Log.d("HomeViewModel", "User loaded: $userData")
        }
    }
    fun logOut(){
        auth.logoutUser()
    }
}