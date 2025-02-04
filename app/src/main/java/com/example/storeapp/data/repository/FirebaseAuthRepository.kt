package com.example.storeapp.data.repository

import android.util.Log
import com.example.storeapp.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Đăng ký người dùng mới và lưu vào Firestore
    suspend fun registerUser(email: String, password: String, userModel: UserModel): AuthResult? {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                // Lưu thông tin người dùng vào Firestore
                val userMap = hashMapOf(
                    "id" to user.uid,
                    "email" to email,
                    "firstName" to userModel.firstName,
                    "lastName" to userModel.lastName,
                    "phone" to userModel.phone,
                    "fullAddress" to userModel.fullAddress,
//                     Thêm các thuộc tính khác từ UserModel
                    "createdAt" to userModel.createdAt,
                    "updatedAt" to userModel.updatedAt
                )
                firestore.collection("Users").document(user.uid).set(userMap).await()
            }
            Log.d("FirebaseAuthRepository", "Registration successful: ${user?.uid}")
            result
        } catch (e: Exception) {
            Log.e("FirebaseAuthRepository", "Registration failed: ${e.message}")
            null
        }
    }

    // Đăng nhập người dùng và lấy thông tin từ Firestore
    suspend fun loginUser(email: String, password: String): AuthResult? {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                // Lấy thông tin người dùng từ Firestore
                val userDoc = firestore.collection("users").document(user.uid).get().await()
                val userModel = userDoc.toObject(UserModel::class.java)
                Log.d("FirebaseAuthRepository", "User info: $userModel")
            }
            Log.d("FirebaseAuthRepository", "Login successful: ${user?.uid}")
            result
        } catch (e: Exception) {
            Log.e("FirebaseAuthRepository", "Login failed: ${e.message}")
            null
        }
    }

    // Đăng xuất
    fun logoutUser() {
        auth.signOut()
        Log.d("FirebaseAuthRepository", "User logged out")
    }

    // Lấy thông tin người dùng hiện tại
    fun getCurrentUser() = auth.currentUser
}
