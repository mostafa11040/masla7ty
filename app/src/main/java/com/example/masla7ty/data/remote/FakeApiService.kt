package com.example.maslahty.data.remote

import com.example.maslahty.data.models.AuthTokensDto
import com.example.maslahty.data.models.OTPResponseDto
import com.example.maslahty.data.models.OTPVerificationDto
import com.example.maslahty.data.models.PriceWarningDto
import com.example.maslahty.data.models.TransferRequestDto
import com.example.maslahty.data.models.UserDto
import com.example.maslahty.data.models.VehicleDto
import com.example.maslahty.data.models.ViolationDto
import java.util.UUID
import okhttp3.MultipartBody

/**
 * Simple in-memory API to keep the app fully runnable without backend availability.
 */
class FakeApiService : ApiService {

    private val usersById = linkedMapOf<String, UserDto>()
    private val usersByNationalId = linkedMapOf<String, UserDto>()
    private val vehiclesById = linkedMapOf<String, VehicleDto>()
    private val vehiclesByPlate = linkedMapOf<String, VehicleDto>()
    private val requestsById = linkedMapOf<String, TransferRequestDto>()
    private val violationsById = linkedMapOf<String, ViolationDto>()
    private val otpByPhone = linkedMapOf<String, String>()

    init {
        seedData()
    }

    override suspend fun sendOTP(request: Map<String, String>): OTPResponseDto {
        val nationalId = request["national_id"].orEmpty()
        val phoneNumber = request["phone_number"].orEmpty()
        val user = usersByNationalId[nationalId]
            ?: throw IllegalArgumentException("لا يوجد مستخدم بهذا الرقم القومي")

        if (user.phoneNumber != phoneNumber) {
            throw IllegalArgumentException("رقم الهاتف غير مطابق للبيانات الحكومية")
        }

        val code = "123456"
        otpByPhone[phoneNumber] = code

        return OTPResponseDto(
            phoneNumber = phoneNumber,
            expiryTime = System.currentTimeMillis() + (2 * 60 * 1000),
            message = "OTP sent successfully"
        )
    }

    override suspend fun verifyOTP(request: OTPVerificationDto): AuthTokensDto {
        val expectedOtp = otpByPhone[request.phoneNumber]
            ?: throw IllegalStateException("OTP غير مرسل لهذا الرقم")

        if (request.code != expectedOtp) {
            throw IllegalArgumentException("رمز التحقق غير صحيح")
        }

        val user = usersById.values.firstOrNull { it.phoneNumber == request.phoneNumber }
            ?: throw IllegalArgumentException("المستخدم غير موجود")

        return AuthTokensDto(
            accessToken = "fake_access_${UUID.randomUUID()}",
            refreshToken = "fake_refresh_${UUID.randomUUID()}",
            expiresIn = 3600,
            user = user
        )
    }

    override suspend fun registerUser(user: UserDto): UserDto {
        val toSave = user.copy(id = if (user.id.isBlank()) "user_${UUID.randomUUID()}" else user.id)
        usersById[toSave.id] = toSave
        usersByNationalId[toSave.nationalId] = toSave
        return toSave
    }

    override suspend fun getUser(id: String): UserDto {
        return usersById[id] ?: throw IllegalArgumentException("المستخدم غير موجود")
    }

    override suspend fun getUserByNationalId(nationalId: String): UserDto {
        return usersByNationalId[nationalId] ?: throw IllegalArgumentException("المستخدم غير موجود")
    }

    override suspend fun updateUser(id: String, user: UserDto): UserDto {
        val existing = usersById[id] ?: throw IllegalArgumentException("المستخدم غير موجود")
        val updated = user.copy(id = existing.id)
        usersById[id] = updated
        usersByNationalId.values.removeIf { it.id == id }
        usersByNationalId[updated.nationalId] = updated
        return updated
    }

    override suspend fun createVehicle(vehicle: VehicleDto): VehicleDto {
        val vehicleId = if (vehicle.id.isBlank()) "vehicle_${UUID.randomUUID()}" else vehicle.id
        val toSave = vehicle.copy(id = vehicleId)
        vehiclesById[toSave.id] = toSave
        vehiclesByPlate[toSave.licensePlate] = toSave
        return toSave
    }

    override suspend fun getVehicle(id: String): VehicleDto {
        return vehiclesById[id] ?: throw IllegalArgumentException("المركبة غير موجودة")
    }

    override suspend fun getVehicleByPlate(plate: String): VehicleDto {
        val normalized = plate.replace("-", "").replace(" ", "").uppercase()
        return vehiclesByPlate[normalized] ?: throw IllegalArgumentException("المركبة غير موجودة")
    }

    override suspend fun getUserVehicles(userId: String): List<VehicleDto> {
        return vehiclesById.values.filter { it.ownerId == userId }
    }

    override suspend fun updateVehicle(id: String, vehicle: VehicleDto): VehicleDto {
        val existing = vehiclesById[id] ?: throw IllegalArgumentException("المركبة غير موجودة")
        val updated = vehicle.copy(id = existing.id)
        vehiclesById[id] = updated
        vehiclesByPlate.values.removeIf { it.id == id }
        vehiclesByPlate[updated.licensePlate] = updated
        return updated
    }

    override suspend fun uploadImage(file: MultipartBody.Part): Map<String, String> {
        return mapOf("url" to "https://mock.local/images/${UUID.randomUUID()}.jpg")
    }

    override suspend fun createTransferRequest(request: TransferRequestDto): TransferRequestDto {
        val requestId = if (request.id.isBlank()) "request_${UUID.randomUUID()}" else request.id
        val buyerUser = usersById[request.buyerId] ?: usersByNationalId[request.buyerId]
        val normalizedBuyerId = buyerUser?.id ?: request.buyerId
        val normalizedBuyerName = buyerUser?.fullName ?: request.buyerName

        val toSave = request.copy(
            id = requestId,
            buyerId = normalizedBuyerId,
            buyerName = normalizedBuyerName
        )
        requestsById[toSave.id] = toSave
        return toSave
    }

    override suspend fun getTransferRequest(id: String): TransferRequestDto {
        return requestsById[id] ?: throw IllegalArgumentException("طلب النقل غير موجود")
    }

    override suspend fun getSellerRequests(sellerId: String): List<TransferRequestDto> {
        return requestsById.values.filter { it.sellerId == sellerId }
    }

    override suspend fun getBuyerRequests(buyerId: String): List<TransferRequestDto> {
        return requestsById.values.filter {
            it.buyerId == buyerId || usersByNationalId[buyerId]?.id == it.buyerId
        }
    }

    override suspend fun approveTransferRequest(id: String, data: Map<String, String>): TransferRequestDto {
        val existing = requestsById[id] ?: throw IllegalArgumentException("طلب النقل غير موجود")
        val buyerId = data["buyer_id"] ?: existing.buyerId
        val status = data["status"] ?: "APPROVED_BY_BUYER"
        val buyer = usersById[buyerId]

        val updated = existing.copy(
            buyerId = buyerId,
            buyerName = buyer?.fullName ?: existing.buyerName,
            status = status,
            updatedAt = System.currentTimeMillis(),
            priceWarning = existing.priceWarning ?: buildPriceWarning(existing.price)
        )
        requestsById[id] = updated
        return updated
    }

    override suspend fun rejectTransferRequest(id: String, data: Map<String, String>): Map<String, String> {
        val existing = requestsById[id] ?: throw IllegalArgumentException("طلب النقل غير موجود")
        requestsById[id] = existing.copy(status = "REJECTED_BY_BUYER", updatedAt = System.currentTimeMillis())
        return mapOf("message" to "Transfer request rejected")
    }

    // ── Violation Endpoints ─────────────────────────────
    override suspend fun getVehicleViolations(vehicleId: String): List<ViolationDto> {
        return violationsById.values.filter { it.vehicleId == vehicleId }
    }

    override suspend fun getViolationById(id: String): ViolationDto {
        return violationsById[id] ?: throw IllegalArgumentException("المخالفة غير موجودة")
    }

    private fun buildPriceWarning(price: Double): PriceWarningDto? {
        val marketPrice = 100000.0
        val difference = price - marketPrice
        val percentage = (difference / marketPrice) * 100
        return if (kotlin.math.abs(percentage) > 30) {
            PriceWarningDto(
                marketPrice = marketPrice,
                difference = difference,
                percentage = percentage,
                message = "تحذير: السعر بعيد عن متوسط السوق"
            )
        } else {
            null
        }
    }

    private fun seedData() {
        val now = System.currentTimeMillis()

        val seller = UserDto(
            id = "user1",
            nationalId = "1234567890",
            fullName = "أحمد محمد",
            email = "seller@example.com",
            phoneNumber = "966501234567",
            userType = "SELLER",
            profileImageUrl = null,
            createdAt = now,
            isVerified = true,
            address = "القاهرة",
            city = "القاهرة"
        )

        val buyer = UserDto(
            id = "user2",
            nationalId = "9876543210",
            fullName = "محمد علي",
            email = "buyer@example.com",
            phoneNumber = "966551234567",
            userType = "BUYER",
            profileImageUrl = null,
            createdAt = now,
            isVerified = true,
            address = "الجيزة",
            city = "الجيزة"
        )

        usersById[seller.id] = seller
        usersById[buyer.id] = buyer
        usersByNationalId[seller.nationalId] = seller
        usersByNationalId[buyer.nationalId] = buyer

        val vehicle = VehicleDto(
            id = "vehicle1",
            ownerId = seller.id,
            licensePlate = "100ABC1234",
            chassisNumber = "LSVJH5H08EJ123456",
            engineNumber = "123456789AB",
            model = "Toyota Camry",
            manufacturingYear = 2020,
            color = "Silver",
            kilometers = 45000,
            condition = "VERY_GOOD",
            licenseImageUrl = null,
            vehicleImageUrl = null,
            chassisImageUrl = null,
            engineImageUrl = null,
            createdAt = now,
            updatedAt = now
        )

        vehiclesById[vehicle.id] = vehicle
        vehiclesByPlate[vehicle.licensePlate] = vehicle

        // ── Second vehicle for the seller ─────────────────────
        val vehicle2 = VehicleDto(
            id = "vehicle2",
            ownerId = seller.id,
            licensePlate = "200XYZ5678",
            chassisNumber = "WVWZZZ3CZWE123456",
            engineNumber = "987654321CD",
            model = "Hyundai Elantra",
            manufacturingYear = 2022,
            color = "White",
            kilometers = 22000,
            condition = "EXCELLENT",
            licenseImageUrl = null,
            vehicleImageUrl = null,
            chassisImageUrl = null,
            engineImageUrl = null,
            createdAt = now,
            updatedAt = now
        )
        vehiclesById[vehicle2.id] = vehicle2
        vehiclesByPlate[vehicle2.licensePlate] = vehicle2

        val vehicle3 = VehicleDto(
            id = "vehicle3",
            ownerId = seller.id,
            licensePlate = "300DEF9012",
            chassisNumber = "1HGCM82633A004352",
            engineNumber = "456789123EF",
            model = "Nissan Sunny",
            manufacturingYear = 2018,
            color = "Black",
            kilometers = 78000,
            condition = "GOOD",
            licenseImageUrl = null,
            vehicleImageUrl = null,
            chassisImageUrl = null,
            engineImageUrl = null,
            createdAt = now,
            updatedAt = now
        )
        vehiclesById[vehicle3.id] = vehicle3
        vehiclesByPlate[vehicle3.licensePlate] = vehicle3

        // ── Mock Violations ─────────────────────────────────
        val violation1 = ViolationDto(
            id = "viol_1",
            vehicleId = "vehicle1",
            violationType = "تجاوز السرعة",
            description = "تجاوز السرعة المقررة بـ 40 كم/س على الطريق الدائري",
            date = now - (30L * 24 * 60 * 60 * 1000), // 30 days ago
            amount = 1500.0,
            status = "UNPAID",
            location = "الطريق الدائري - القاهرة"
        )
        val violation2 = ViolationDto(
            id = "viol_2",
            vehicleId = "vehicle1",
            violationType = "مخالفة وقوف",
            description = "وقوف في منطقة ممنوع فيها الانتظار",
            date = now - (60L * 24 * 60 * 60 * 1000), // 60 days ago
            amount = 500.0,
            status = "PAID",
            location = "شارع التحرير - وسط البلد"
        )
        val violation3 = ViolationDto(
            id = "viol_3",
            vehicleId = "vehicle1",
            violationType = "قطع إشارة مرور",
            description = "عدم الالتزام بالإشارة الحمراء عند التقاطع",
            date = now - (15L * 24 * 60 * 60 * 1000), // 15 days ago
            amount = 2000.0,
            status = "UNPAID",
            location = "ميدان رمسيس - القاهرة"
        )
        val violation4 = ViolationDto(
            id = "viol_4",
            vehicleId = "vehicle3",
            violationType = "رخصة منتهية",
            description = "قيادة السيارة برخصة منتهية الصلاحية",
            date = now - (5L * 24 * 60 * 60 * 1000), // 5 days ago
            amount = 1000.0,
            status = "UNPAID",
            location = "طريق المعادي - القاهرة"
        )

        violationsById[violation1.id] = violation1
        violationsById[violation2.id] = violation2
        violationsById[violation3.id] = violation3
        violationsById[violation4.id] = violation4
    }
}

