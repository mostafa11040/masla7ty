package com.example.maslahty.domain.usecases.transfer

import com.example.maslahty.domain.common.Result
import com.example.maslahty.domain.entities.TransferRequest
import com.example.maslahty.domain.repositories.TransferRequestRepository

class ApproveTransferRequestUseCase(private val transferRequestRepository: TransferRequestRepository) {
    suspend operator fun invoke(requestId: String, buyerId: String): Result<TransferRequest> {
        return transferRequestRepository.approveTransferRequest(requestId, buyerId)
    }
}

