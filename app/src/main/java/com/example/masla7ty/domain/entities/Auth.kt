package com.example.maslahty.domain.entities

data class OTPData(
    val phoneNumber: String,
    val code: String,
    val expiryTime: Long,
    val attempts: Int = 0
)

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)

