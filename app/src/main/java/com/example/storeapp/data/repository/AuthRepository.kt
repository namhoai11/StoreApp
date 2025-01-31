package com.example.storeapp.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    suspend fun getCurrentUser(): User? {
        return auth.currentUser?.let {
            User(it.uid, it.email)
        }
    }

    suspend fun login(email: String, password: String): User? {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user?.let {
            User(it.uid, it.email)
        }
    }

    suspend fun logout() {
        auth.signOut()
    }
}

data class User(
    val uid: String,
    val email: String?
)