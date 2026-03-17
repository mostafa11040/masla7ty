package com.example.maslahty.data.mappers

import com.example.maslahty.data.local.entities.UserEntity
import com.example.maslahty.data.models.UserDto
import com.example.maslahty.domain.entities.User
import com.example.maslahty.domain.entities.UserType
import java.util.Date

fun UserDto.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        nationalId = nationalId,
        fullName = fullName,
        email = email,
        phoneNumber = phoneNumber,
        userType = userType,
        profileImageUrl = profileImageUrl,
        createdAt = createdAt,
        isVerified = isVerified,
        address = address,
        city = city
    )
}

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        nationalId = nationalId,
        fullName = fullName,
        email = email,
        phoneNumber = phoneNumber,
        userType = UserType.valueOf(userType),
        profileImageUrl = profileImageUrl,
        createdAt = Date(createdAt),
        isVerified = isVerified,
        address = address,
        city = city
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        nationalId = nationalId,
        fullName = fullName,
        email = email,
        phoneNumber = phoneNumber,
        userType = userType.name,
        profileImageUrl = profileImageUrl,
        createdAt = createdAt.time,
        isVerified = isVerified,
        address = address,
        city = city
    )
}

fun UserDto.toDomain(): User {
    return User(
        id = id,
        nationalId = nationalId,
        fullName = fullName,
        email = email,
        phoneNumber = phoneNumber,
        userType = UserType.valueOf(userType),
        profileImageUrl = profileImageUrl,
        createdAt = Date(createdAt),
        isVerified = isVerified,
        address = address,
        city = city
    )
}

