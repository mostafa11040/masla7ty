package com.example.maslahty.domain.usecases.transfer

import com.example.maslahty.domain.common.Result
import com.example.maslahty.domain.entities.TransferRequest
import com.example.maslahty.domain.repositories.TransferRequestRepository

class CreateTransferRequestUseCase(private val transferRequestRepository: TransferRequestRepository) {
    suspend operator fun invoke(request: TransferRequest): Result<TransferRequest> {
        return transferRequestRepository.createTransferRequest(request)
    }
}

