package com.example.maslahty.domain.usecases.violation

import com.example.maslahty.domain.common.Result
import com.example.maslahty.domain.repositories.ViolationRepository

class CheckViolationsForTransferUseCase(private val violationRepository: ViolationRepository) {
    suspend operator fun invoke(vehicleId: String): Result<Boolean> {
        return violationRepository.hasUnpaidViolations(vehicleId)
    }
}
