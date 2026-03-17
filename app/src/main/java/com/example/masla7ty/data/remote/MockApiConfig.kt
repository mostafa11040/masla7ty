package com.example.maslahty.data.remote

/**
 * Mock Configuration for Testing
 * 
 * This file contains configuration for mock API responses
 * Replace these with actual API endpoints in production
 */

object MockApiConfig {
    
    const val BASE_URL = "https://api.example.com/"
    const val TIMEOUT_SECONDS = 30L
    
    // Mock Endpoints (for testing without backend)
    const val MOCK_MODE = false  // Set to true to use mock responses
    
    object MockData {
        val mockUsers = mapOf(
            "1234567890" to MockUserResponse(
                id = "user1",
                nationalId = "1234567890",
                fullName = "أحمد محمد",
                email = "ahmed@example.com",
                phoneNumber = "966501234567",
                userType = "SELLER"
            )
        )
        
        val mockVehicles = listOf(
            MockVehicleResponse(
                id = "vehicle1",
                ownerId = "user1",
                licensePlate = "100ABC1234",
                chassisNumber = "LSVJH5H08EJ123456",
                engineNumber = "123456789",
                model = "Toyota Camry",
                manufacturingYear = 2020,
                color = "Silver"
            )
        )
    }
}

data class MockUserResponse(
    val id: String,
    val nationalId: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val userType: String
)

data class MockVehicleResponse(
    val id: String,
    val ownerId: String,
    val licensePlate: String,
    val chassisNumber: String,
    val engineNumber: String,
    val model: String,
    val manufacturingYear: Int,
    val color: String
)

/**
 * Interceptor untuk mock API responses
 * 
 * Usage:
 * val mockInterceptor = MockApiInterceptor()
 * val okHttpClient = OkHttpClient.Builder()
 *     .addNetworkInterceptor(mockInterceptor)
 *     .build()
 */
object MockApiInterceptor {
    // Implementation akan ditambahkan untuk mock responses
}

