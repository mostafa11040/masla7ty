package com.example.maslahty.data.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.maslahty.data.local.dao.UserDao
import com.example.maslahty.data.mappers.toDomain
import com.example.maslahty.data.mappers.toEntity
import com.example.maslahty.data.models.AuthTokensDto
import com.example.maslahty.data.models.OTPVerificationDto
import com.example.maslahty.data.models.UserDto
import com.example.maslahty.data.remote.ApiService
import com.example.maslahty.domain.common.NetworkException
import com.example.maslahty.domain.common.Result
import com.example.maslahty.domain.entities.AuthTokens
import com.example.maslahty.domain.entities.OTPData
import com.example.maslahty.domain.entities.User
import com.example.maslahty.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_preferences")

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val context: Context
) : AuthRepository {

    private val accessTokenKey = stringPreferencesKey("access_token")
    private val refreshTokenKey = stringPreferencesKey("refresh_token")
    private val userIdKey = stringPreferencesKey("user_id")

    override suspend fun sendOTP(nationalId: String, phoneNumber: String): Result<OTPData> {
        return try {
            val response = apiService.sendOTP(
                mapOf(
                    "national_id" to nationalId,
                    "phone_number" to phoneNumber
                )
            )
            Result.Success(
                OTPData(
                    phoneNumber = response.phoneNumber,
                    code = "",
                    expiryTime = response.expiryTime
                )
            )
        } catch (e: Exception) {
            Result.Error(NetworkException("Failed to send OTP: ${e.message}"))
        }
    }

    override suspend fun verifyOTP(phoneNumber: String, code: String): Result<AuthTokens> {
        return try {
            val response = apiService.verifyOTP(OTPVerificationDto(phoneNumber, code))
            saveAuthTokens(response.accessToken, response.refreshToken, response.user.id)
            userDao.insertUser(response.user.toEntity())
            Result.Success(
                AuthTokens(
                    accessToken = response.accessToken,
                    refreshToken = response.refreshToken,
                    expiresIn = response.expiresIn
                )
            )
        } catch (e: Exception) {
            Result.Error(NetworkException("Failed to verify OTP: ${e.message}"))
        }
    }

    override suspend fun registerUser(user: User): Result<User> {
        return try {
            val userDto = UserDto(
                user.id, user.nationalId, user.fullName, user.email, user.phoneNumber,
                user.userType.name, user.profileImageUrl, user.createdAt.time, user.isVerified,
                user.address, user.city
            )
            val response = apiService.registerUser(userDto)
            userDao.insertUser(response.toEntity())
            Result.Success(response.toDomain())
        } catch (e: Exception) {
            Result.Error(NetworkException("Failed to register user: ${e.message}"))
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            context.dataStore.edit { preferences ->
                preferences.remove(accessTokenKey)
                preferences.remove(refreshTokenKey)
                preferences.remove(userIdKey)
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(NetworkException("Failed to logout: ${e.message}"))
        }
    }

    override fun isLoggedIn(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[accessTokenKey] != null
        }
    }

    override suspend fun getCurrentUser(): Result<User> {
        return try {
            val userId = context.dataStore.data.map { it[userIdKey] }.first()
            if (userId != null) {
                val userEntity = userDao.getUserById(userId)
                if (userEntity != null) {
                    Result.Success(userEntity.toDomain())
                } else {
                    Result.Error(NetworkException("User not found"))
                }
            } else {
                Result.Error(NetworkException("No logged in user"))
            }
        } catch (e: Exception) {
            Result.Error(NetworkException("Failed to get current user: ${e.message}"))
        }
    }

    private suspend fun saveAuthTokens(accessToken: String, refreshToken: String, userId: String) {
        context.dataStore.edit { preferences ->
            preferences[accessTokenKey] = accessToken
            preferences[refreshTokenKey] = refreshToken
            preferences[userIdKey] = userId
        }
    }
}

// Extension function to get the first value from Flow
suspend inline fun <T> Flow<T>.first(): T {
    var result: T? = null
    collect { value ->
        if (result == null) result = value
    }
    return result ?: throw NoSuchElementException("Flow is empty")
}

