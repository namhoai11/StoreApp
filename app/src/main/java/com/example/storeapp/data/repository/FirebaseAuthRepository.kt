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

    // Đăng ký người dùng mới
    suspend fun registerUser(email: String, password: String, userModel: UserModel): Result<AuthResult> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                val userMap = mapOf(
                    "id" to user.uid,
                    "email" to email,
                    "firstName" to userModel.firstName,
                    "lastName" to userModel.lastName,
                    "dateOfBirt" to userModel.dateOfBirth,
                    "gender" to userModel.gender,
                    "phone" to userModel.phone,
                    "createdAt" to userModel.createdAt,
                    "updatedAt" to userModel.updatedAt
                )
                firestore.collection("Users").document(user.uid).set(userMap).await()
//                // Gửi email xác nhận
//                sendEmailVerification()
            }
            Log.d("FirebaseAuthRepository", "Registration successful: ${user?.uid}")
            Result.success(result)
        } catch (e: Exception) {
            Log.e("FirebaseAuthRepository", "Registration failed: ${e.message}")
            Result.failure(e)
        }
    }

//    private suspend fun sendEmailVerification(): Result<Unit> {
//        return try {
//            auth.currentUser?.sendEmailVerification()?.await()
//            Log.d("FirebaseAuthRepository", "Email verification sent")
//            Result.success(Unit)
//        } catch (e: Exception) {
//            Log.e("FirebaseAuthRepository", "Failed to send email verification: ${e.message}")
//            Result.failure(e)
//        }
//    }
    // Đăng nhập
    suspend fun loginUser(email: String, password: String): Result<AuthResult> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            if (auth.currentUser?.isEmailVerified == false) {
                return Result.failure(Exception("Email chưa được xác nhận. Vui lòng kiểm tra hộp thư."))
            }
            Result.success(result)
        } catch (e: Exception) {
            Log.e("FirebaseAuthRepository", "Login failed: ${e.message}")
            Result.failure(e)
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

