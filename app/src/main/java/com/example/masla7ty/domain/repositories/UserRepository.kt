package com.example.maslahty.domain.repositories

import com.example.maslahty.domain.common.Result
import com.example.maslahty.domain.entities.User

interface UserRepository {
    suspend fun getUserById(id: String): Result<User>
    suspend fun getUserByNationalId(nationalId: String): Result<User>
    suspend fun createUser(user: User): Result<User>
    suspend fun updateUser(user: User): Result<User>
    suspend fun deleteUser(id: String): Result<Unit>
}

