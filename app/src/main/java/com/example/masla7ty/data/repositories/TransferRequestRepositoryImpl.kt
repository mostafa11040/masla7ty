package com.example.maslahty.data.repositories

import com.example.maslahty.data.local.dao.TransferRequestDao
import com.example.maslahty.data.mappers.toDomain
import com.example.maslahty.data.mappers.toEntity
import com.example.maslahty.data.models.TransferRequestDto
import com.example.maslahty.data.remote.ApiService
import com.example.maslahty.domain.common.NetworkException
import com.example.maslahty.domain.common.Result
import com.example.maslahty.domain.entities.TransferRequest
import com.example.maslahty.domain.repositories.TransferRequestRepository

class TransferRequestRepositoryImpl(
    private val apiService: ApiService,
    private val transferRequestDao: TransferRequestDao
) : TransferRequestRepository {

    override suspend fun createTransferRequest(request: TransferRequest): Result<TransferRequest> {
        return try {
            val requestEntity = request.toEntity()
            val requestDto = TransferRequestDto(
                requestEntity.id, requestEntity.vehicleId, requestEntity.sellerId,
                requestEntity.buyerId, requestEntity.price, requestEntity.status,
                requestEntity.sellerName, requestEntity.buyerName,
                requestEntity.createdAt, requestEntity.updatedAt, requestEntity.notes,
                null
            )
            val response = apiService.createTransferRequest(requestDto)
            transferRequestDao.insertTransferRequest(response.toEntity())
            Result.Success(response.toDomain())
        } catch (e: Exception) {
            Result.Error(NetworkException("Failed to create transfer request: ${e.message}"))
        }
    }

    override suspend fun getTransferRequestById(id: String): Result<TransferRequest> {
        return try {
            val requestEntity = transferRequestDao.getTransferRequestById(id)
            if (requestEntity != null) {
                Result.Success(requestEntity.toDomain())
            } else {
                val requestDto = apiService.getTransferRequest(id)
                transferRequestDao.insertTransferRequest(requestDto.toEntity())
                Result.Success(requestDto.toDomain())
            }
        } catch (e: Exception) {
            Result.Error(NetworkException("Failed to get transfer request: ${e.message}"))
        }
    }

    override suspend fun getSellerRequests(sellerId: String): Result<List<TransferRequest>> {
        return try {
            val requestDtos = apiService.getSellerRequests(sellerId)
            val requests = requestDtos.map {
                transferRequestDao.insertTransferRequest(it.toEntity())
                it.toDomain()
            }
            Result.Success(requests)
        } catch (e: Exception) {
            Result.Error(NetworkException("Failed to get seller requests: ${e.message}"))
        }
    }

    override suspend fun getBuyerRequests(buyerId: String): Result<List<TransferRequest>> {
        return try {
            val requestDtos = apiService.getBuyerRequests(buyerId)
            val requests = requestDtos.map {
                transferRequestDao.insertTransferRequest(it.toEntity())
                it.toDomain()
            }
            Result.Success(requests)
        } catch (e: Exception) {
            Result.Error(NetworkException("Failed to get buyer requests: ${e.message}"))
        }
    }

    override suspend fun updateTransferRequest(request: TransferRequest): Result<TransferRequest> {
        return try {
            val requestEntity = request.toEntity()
            val requestDto = TransferRequestDto(
                requestEntity.id, requestEntity.vehicleId, requestEntity.sellerId,
                requestEntity.buyerId, requestEntity.price, requestEntity.status,
                requestEntity.sellerName, requestEntity.buyerName,
                requestEntity.createdAt, requestEntity.updatedAt, requestEntity.notes,
                null
            )
            val response = apiService.approveTransferRequest(request.id, mapOf("status" to request.status.name))
            transferRequestDao.updateTransferRequest(response.toEntity())
            Result.Success(response.toDomain())
        } catch (e: Exception) {
            Result.Error(NetworkException("Failed to update transfer request: ${e.message}"))
        }
    }

    override suspend fun approveTransferRequest(requestId: String, buyerId: String): Result<TransferRequest> {
        return try {
            val response = apiService.approveTransferRequest(requestId, mapOf("buyer_id" to buyerId))
            transferRequestDao.insertTransferRequest(response.toEntity())
            Result.Success(response.toDomain())
        } catch (e: Exception) {
            Result.Error(NetworkException("Failed to approve transfer request: ${e.message}"))
        }
    }

    override suspend fun rejectTransferRequest(requestId: String, reason: String): Result<Unit> {
        return try {
            apiService.rejectTransferRequest(requestId, mapOf("reason" to reason))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(NetworkException("Failed to reject transfer request: ${e.message}"))
        }
    }
}

