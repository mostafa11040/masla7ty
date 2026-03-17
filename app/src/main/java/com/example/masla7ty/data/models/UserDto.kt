package com.example.maslahty.data.models

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("national_id")
    val nationalId: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("user_type")
    val userType: String,
    @SerializedName("profile_image_url")
    val profileImageUrl: String? = null,
    @SerializedName("created_at")
    val createdAt: Long,
    @SerializedName("is_verified")
    val isVerified: Boolean = false,
    @SerializedName("address")
    val address: String = "",
    @SerializedName("city")
    val city: String = ""
)

