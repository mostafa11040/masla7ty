package com.example.maslahty.domain.usecases.transfer

import com.example.maslahty.domain.common.Result
import com.example.maslahty.domain.entities.TransferRequest
import com.example.maslahty.domain.repositories.TransferRequestRepository

class GetBuyerRequestsUseCase(private val transferRequestRepository: TransferRequestRepository) {
    suspend operator fun invoke(buyerId: String): Result<List<TransferRequest>> {
        return transferRequestRepository.getBuyerRequests(buyerId)
    }
}

