package com.example.maslahty.domain.utils

/**
 * Test Data Factory
 * يحتوي على بيانات وهمية للاختبار والتطوير
 */

import com.example.maslahty.domain.entities.User
import com.example.maslahty.domain.entities.UserType
import com.example.maslahty.domain.entities.Vehicle
import com.example.maslahty.domain.entities.VehicleCondition
import com.example.maslahty.domain.entities.TransferRequest
import com.example.maslahty.domain.entities.TransferStatus
import java.util.Date

object TestDataFactory {
    
    fun createTestUser(
        id: String = "user_1",
        nationalId: String = "1234567890",
        userType: UserType = UserType.SELLER
    ) = User(
        id = id,
        nationalId = nationalId,
        fullName = "أحمد محمد علي",
        email = "ahmed@example.com",
        phoneNumber = "966501234567",
        userType = userType,
        profileImageUrl = null,
        createdAt = Date(),
        isVerified = true,
        address = "شارع النيل، القاهرة",
        city = "القاهرة"
    )
    
    fun createTestVehicle(
        id: String = "vehicle_1",
        ownerId: String = "user_1",
        licensePlate: String = "100ABC1234"
    ) = Vehicle(
        id = id,
        ownerId = ownerId,
        licensePlate = licensePlate,
        chassisNumber = "LSVJH5H08EJ123456",
        engineNumber = "123456789AB",
        model = "Toyota Camry",
        manufacturingYear = 2020,
        color = "Silver",
        kilometers = 15000,
        condition = VehicleCondition.EXCELLENT,
        licenseImageUrl = "https://example.com/license.jpg",
        vehicleImageUrl = "https://example.com/vehicle.jpg",
        chassisImageUrl = "https://example.com/chassis.jpg",
        engineImageUrl = "https://example.com/engine.jpg",
        createdAt = Date(),
        updatedAt = Date()
    )
    
    fun createTestTransferRequest(
        id: String = "transfer_1",
        vehicleId: String = "vehicle_1",
        sellerId: String = "user_1",
        buyerId: String = "user_2"
    ) = TransferRequest(
        id = id,
        vehicleId = vehicleId,
        sellerId = sellerId,
        buyerId = buyerId,
        price = 85000.0,
        status = TransferStatus.PENDING,
        sellerName = "أحمد محمد",
        buyerName = "فاطمة علي",
        createdAt = Date(),
        updatedAt = Date(),
        notes = "حالة ممتازة، لم تقع أي حوادث",
        priceWarning = null
    )
    
    // Test cases للتحقق من البيانات
    fun getValidNationalIds() = listOf(
        "1234567890",
        "9876543210",
        "1111111111"
    )
    
    fun getValidPhoneNumbers() = listOf(
        "966501234567",
        "966551234567",
        "201001234567"
    )
    
    fun getValidOTPCodes() = listOf(
        "123456",
        "000000",
        "999999"
    )
    
    fun getValidLicensePlates() = listOf(
        "100ABC1234",
        "200XYZ9876",
        "050KLM5432"
    )
    
    fun getInvalidNationalIds() = listOf(
        "123",
        "abcdefghij",
        "",
        "12345678901"
    )
    
    fun getInvalidPhoneNumbers() = listOf(
        "123",
        "abc",
        "",
        "1".repeat(20)
    )
    
    fun getPriceTestCases() = listOf(
        Pair(100000.0, 85000.0),  // Price 15% below market
        Pair(100000.0, 130000.0), // Price 30% above market (warning)
        Pair(100000.0, 100000.0), // Price equal to market
        Pair(100000.0, 95000.0),  // Price 5% below market
        Pair(100000.0, 105000.0)  // Price 5% above market
    )
}

/**
 * مثال على الاستخدام:
 * 
 * val testUser = TestDataFactory.createTestUser()
 * val testVehicle = TestDataFactory.createTestVehicle()
 * val testTransfer = TestDataFactory.createTestTransferRequest()
 */

