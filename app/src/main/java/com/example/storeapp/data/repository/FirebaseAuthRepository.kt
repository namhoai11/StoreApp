package com.example.storeapp.data.repository

import android.util.Log
import com.example.storeapp.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
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


    suspend fun updateUserPassword(oldPassword: String, newPassword: String): Result<Unit> {
        return try {
            val user = auth.currentUser ?: return Result.failure(Exception("Người dùng chưa đăng nhập"))
            val email = user.email ?: return Result.failure(Exception("Không tìm thấy email người dùng"))

            // Xác thực mật khẩu cũ trước khi thay đổi
            val credential = EmailAuthProvider.getCredential(email, oldPassword)
            user.reauthenticate(credential).await()

            // Cập nhật mật khẩu mới
            user.updatePassword(newPassword).await()

            Log.d("FirebaseAuthRepository", "Cập nhật mật khẩu thành công")
            Result.success(Unit)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(Exception("Mật khẩu cũ không chính xác"))
        } catch (e: FirebaseAuthRecentLoginRequiredException) {
            Result.failure(Exception("Vui lòng đăng nhập lại để thay đổi thông tin"))
        } catch (e: Exception) {
            Log.e("FirebaseAuthRepository", "Lỗi khi cập nhật mật khẩu", e)
            Result.failure(e)
        }
    }
    // Gửi email đặt lại mật khẩu
    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Log.d("FirebaseAuthRepository", "Email đặt lại mật khẩu đã được gửi")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FirebaseAuthRepository", "Lỗi khi gửi email đặt lại mật khẩu", e)
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

