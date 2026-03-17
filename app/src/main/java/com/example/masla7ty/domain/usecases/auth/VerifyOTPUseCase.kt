package com.example.maslahty.domain.usecases.auth

import com.example.maslahty.domain.common.Result
import com.example.maslahty.domain.entities.AuthTokens
import com.example.maslahty.domain.repositories.AuthRepository

class VerifyOTPUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(phoneNumber: String, code: String): Result<AuthTokens> {
        return authRepository.verifyOTP(phoneNumber, code)
    }
}

