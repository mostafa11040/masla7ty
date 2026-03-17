package com.example.maslahty.data.repositories

import com.example.maslahty.data.local.dao.UserDao
import com.example.maslahty.data.mappers.toDomain
import com.example.maslahty.data.mappers.toEntity
import com.example.maslahty.data.remote.ApiService
import com.example.maslahty.domain.common.DatabaseException
import com.example.maslahty.domain.common.NetworkException
import com.example.maslahty.domain.common.Result
import com.example.maslahty.domain.entities.User
import com.example.maslahty.domain.repositories.UserRepository

class UserRepositoryImpl(
    private val apiService: ApiService,
    private val userDao: UserDao
) : UserRepository {

    override suspend fun getUserById(id: String): Result<User> {
        return try {
            val userEntity = userDao.getUserById(id)
            if (userEntity != null) {
                Result.Success(userEntity.toDomain())
            } else {
                val userDto = apiService.getUser(id)
                userDao.insertUser(userDto.toEntity())
                Result.Success(userDto.toDomain())
            }
        } catch (e: Exception) {
            Result.Error(NetworkException("Failed to get user: ${e.message}"))
        }
    }

    override suspend fun getUserByNationalId(nationalId: String): Result<User> {
        return try {
            val userEntity = userDao.getUserByNationalId(nationalId)
            if (userEntity != null) {
                Result.Success(userEntity.toDomain())
            } else {
                val userDto = apiService.getUserByNationalId(nationalId)
                userDao.insertUser(userDto.toEntity())
                Result.Success(userDto.toDomain())
            }
        } catch (e: Exception) {
            Result.Error(NetworkException("Failed to get user: ${e.message}"))
        }
    }

    override suspend fun createUser(user: User): Result<User> {
        return try {
            val userDto = apiService.registerUser(user.toEntity().let {
                com.example.maslahty.data.models.UserDto(
                    it.id, it.nationalId, it.fullName, it.email, it.phoneNumber,
                    it.userType, it.profileImageUrl, it.createdAt, it.isVerified, it.address, it.city
                )
            })
            userDao.insertUser(userDto.toEntity())
            Result.Success(userDto.toDomain())
        } catch (e: Exception) {
            Result.Error(NetworkException("Failed to create user: ${e.message}"))
        }
    }

    override suspend fun updateUser(user: User): Result<User> {
        return try {
            val userDto = apiService.updateUser(user.id, user.toEntity().let {
                com.example.maslahty.data.models.UserDto(
                    it.id, it.nationalId, it.fullName, it.email, it.phoneNumber,
                    it.userType, it.profileImageUrl, it.createdAt, it.isVerified, it.address, it.city
                )
            })
            userDao.updateUser(userDto.toEntity())
            Result.Success(userDto.toDomain())
        } catch (e: Exception) {
            Result.Error(NetworkException("Failed to update user: ${e.message}"))
        }
    }

    override suspend fun deleteUser(id: String): Result<Unit> {
        return try {
            val user = userDao.getUserById(id) ?: return Result.Error(DatabaseException("User not found"))
            userDao.deleteUser(user)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DatabaseException("Failed to delete user: ${e.message}"))
        }
    }
}

