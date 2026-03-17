package com.example.maslahty.domain.repositories

import com.example.maslahty.domain.common.Result
import com.example.maslahty.domain.entities.TransferRequest

interface TransferRequestRepository {
    suspend fun createTransferRequest(request: TransferRequest): Result<TransferRequest>
    suspend fun getTransferRequestById(id: String): Result<TransferRequest>
    suspend fun getSellerRequests(sellerId: String): Result<List<TransferRequest>>
    suspend fun getBuyerRequests(buyerId: String): Result<List<TransferRequest>>
    suspend fun updateTransferRequest(request: TransferRequest): Result<TransferRequest>
    suspend fun approveTransferRequest(requestId: String, buyerId: String): Result<TransferRequest>
    suspend fun rejectTransferRequest(requestId: String, reason: String): Result<Unit>
}

