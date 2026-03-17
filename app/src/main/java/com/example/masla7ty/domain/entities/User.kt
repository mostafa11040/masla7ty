package com.example.maslahty.domain.entities

import java.util.Date

data class User(
    val id: String,
    val nationalId: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val userType: UserType,  // SELLER, BUYER, AUTHORITY
    val profileImageUrl: String? = null,
    val createdAt: Date,
    val isVerified: Boolean = false,
    val address: String = "",
    val city: String = ""
)

enum class UserType {
    SELLER, BUYER, AUTHORITY
}

