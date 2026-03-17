package com.example.maslahty.domain.usecases.auth

import com.example.maslahty.domain.common.Result
import com.example.maslahty.domain.entities.OTPData
import com.example.maslahty.domain.repositories.AuthRepository

class SendOTPUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(nationalId: String, phoneNumber: String): Result<OTPData> {
        return authRepository.sendOTP(nationalId, phoneNumber)
    }
}

