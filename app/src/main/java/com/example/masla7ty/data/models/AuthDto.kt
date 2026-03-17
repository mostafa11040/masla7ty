package com.example.maslahty.data.models

import com.google.gson.annotations.SerializedName

data class OTPResponseDto(
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("expiry_time")
    val expiryTime: Long,
    @SerializedName("message")
    val message: String
)

data class OTPVerificationDto(
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("code")
    val code: String
)

data class AuthTokensDto(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("expires_in")
    val expiresIn: Long,
    @SerializedName("user")
    val user: UserDto
)

