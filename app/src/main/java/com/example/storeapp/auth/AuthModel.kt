//package com.example.storeapp.auth
//
//import com.google.firebase.Timestamp
//
//data class AuthModel(
//    val uid: String,             // ID của user trên Firebase Auth
//    val email: String? = null,   // Email (có thể null nếu user đăng nhập bằng số điện thoại)
//    val displayName: String = "",// Tên hiển thị
//    val phoneNumber: String? = null, // Số điện thoại (nếu có)
//    val photoUrl: String? = null,    // Avatar user
//    val providerId: String,     // Google, Facebook, Email...
//    val createdAt: Timestamp = Timestamp.now(), // Ngày tạo tài khoản
//    val lastLoginAt: Timestamp = Timestamp.now() // Ngày đăng nhập gần nhất
//)
