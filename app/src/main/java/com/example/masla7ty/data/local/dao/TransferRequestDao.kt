package com.example.maslahty.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.maslahty.data.local.entities.TransferRequestEntity

@Dao
interface TransferRequestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransferRequest(request: TransferRequestEntity)

    @Update
    suspend fun updateTransferRequest(request: TransferRequestEntity)

    @Delete
    suspend fun deleteTransferRequest(request: TransferRequestEntity)

    @Query("SELECT * FROM transfer_requests WHERE id = :id")
    suspend fun getTransferRequestById(id: String): TransferRequestEntity?

    @Query("SELECT * FROM transfer_requests WHERE seller_id = :sellerId")
    suspend fun getSellerRequests(sellerId: String): List<TransferRequestEntity>

    @Query("SELECT * FROM transfer_requests WHERE buyer_id = :buyerId")
    suspend fun getBuyerRequests(buyerId: String): List<TransferRequestEntity>

    @Query("SELECT * FROM transfer_requests")
    suspend fun getAllTransferRequests(): List<TransferRequestEntity>
}

