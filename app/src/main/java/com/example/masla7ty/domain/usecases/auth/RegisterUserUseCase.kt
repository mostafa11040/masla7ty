package com.example.maslahty.domain.usecases.auth

import com.example.maslahty.domain.common.Result
import com.example.maslahty.domain.entities.User
import com.example.maslahty.domain.repositories.AuthRepository

class RegisterUserUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(user: User): Result<User> {
        return authRepository.registerUser(user)
    }
}

