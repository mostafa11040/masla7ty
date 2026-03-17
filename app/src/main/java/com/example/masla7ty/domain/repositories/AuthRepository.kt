package com.example.maslahty.domain.repositories

import com.example.maslahty.domain.common.Result
import com.example.maslahty.domain.entities.AuthTokens
import com.example.maslahty.domain.entities.OTPData
import com.example.maslahty.domain.entities.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun sendOTP(nationalId: String, phoneNumber: String): Result<OTPData>
    suspend fun verifyOTP(phoneNumber: String, code: String): Result<AuthTokens>
    suspend fun registerUser(user: User): Result<User>
    suspend fun logout(): Result<Unit>
    fun isLoggedIn(): Flow<Boolean>
    suspend fun getCurrentUser(): Result<User>
}

