package com.example.maslahty.domain.common

class ValidationException(message: String) : Exception(message)
class NetworkException(message: String) : Exception(message)
class DatabaseException(message: String) : Exception(message)
class NotFoundException(message: String) : Exception(message)
class UnauthorizedException(message: String) : Exception(message)

