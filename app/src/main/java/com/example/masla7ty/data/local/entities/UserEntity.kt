package com.example.maslahty.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "national_id")
    val nationalId: String,
    @ColumnInfo(name = "full_name")
    val fullName: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String,
    @ColumnInfo(name = "user_type")
    val userType: String,
    @ColumnInfo(name = "profile_image_url")
    val profileImageUrl: String?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "is_verified")
    val isVerified: Boolean,
    @ColumnInfo(name = "address")
    val address: String,
    @ColumnInfo(name = "city")
    val city: String
)

