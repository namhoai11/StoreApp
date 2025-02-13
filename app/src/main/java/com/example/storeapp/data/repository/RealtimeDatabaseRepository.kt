package com.example.storeapp.data.repository

import android.util.Log
import com.example.storeapp.model.CategoryModel
import com.example.storeapp.model.ProductModel
import com.example.storeapp.model.SliderModel
import com.example.storeapp.model.Stock
import com.google.firebase.Timestamp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date


class RealtimeDatabaseRepository {

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    suspend fun loadStockByProductId(productId: String): Stock? {
        return try {
            val stockSnapshot = firebaseDatabase.getReference("stocks/$productId").get().await()

            // Kiểm tra dữ liệu trả về từ Firebase
            Log.d("FirebaseStoreAppRepository", "Snapshot: ${stockSnapshot.value}")

            val stock = stockSnapshot.getValue(Stock::class.java)

            // Kiểm tra sau khi parse về Stock
            Log.d("FirebaseStoreAppRepository", "Stock parsed: $stock")

            stock
        } catch (e: Exception) {
            Log.e("FirebaseStoreAppRepository", "Lỗi khi load stock", e)
            null
        }
    }

    fun observeStockByProductId(productId: String): Flow<Stock?> = callbackFlow {
        val stockRef = firebaseDatabase.reference.child("stocks").child(productId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val stock = snapshot.getValue(Stock::class.java)
                trySend(stock).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException()) // Đóng Flow khi có lỗi
            }
        }
        stockRef.addValueEventListener(listener)

        awaitClose { stockRef.removeEventListener(listener) } // Xóa listener khi không còn sử dụng
    }

}