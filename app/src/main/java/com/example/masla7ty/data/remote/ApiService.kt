package com.example.maslahty.data.remote

import com.example.maslahty.data.models.AuthTokensDto
import com.example.maslahty.data.models.OTPResponseDto
import com.example.maslahty.data.models.OTPVerificationDto
import com.example.maslahty.data.models.TransferRequestDto
import com.example.maslahty.data.models.UserDto
import com.example.maslahty.data.models.VehicleDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    // Auth Endpoints
    @POST("auth/send-otp")
    suspend fun sendOTP(@Body request: Map<String, String>): OTPResponseDto

    @POST("auth/verify-otp")
    suspend fun verifyOTP(@Body request: OTPVerificationDto): AuthTokensDto

    @POST("auth/register")
    suspend fun registerUser(@Body user: UserDto): UserDto

    // User Endpoints
    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: String): UserDto

    @GET("users/national-id/{nationalId}")
    suspend fun getUserByNationalId(@Path("nationalId") nationalId: String): UserDto

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body user: UserDto): UserDto

    // Vehicle Endpoints
    @POST("vehicles")
    suspend fun createVehicle(@Body vehicle: VehicleDto): VehicleDto

    @GET("vehicles/{id}")
    suspend fun getVehicle(@Path("id") id: String): VehicleDto

    @GET("vehicles/plate/{plate}")
    suspend fun getVehicleByPlate(@Path("plate") plate: String): VehicleDto

    @GET("users/{userId}/vehicles")
    suspend fun getUserVehicles(@Path("userId") userId: String): List<VehicleDto>

    @PUT("vehicles/{id}")
    suspend fun updateVehicle(@Path("id") id: String, @Body vehicle: VehicleDto): VehicleDto

    // Image Upload
    @Multipart
    @POST("images/upload")
    suspend fun uploadImage(@Part file: MultipartBody.Part): Map<String, String>

    // Transfer Request Endpoints
    @POST("transfer-requests")
    suspend fun createTransferRequest(@Body request: TransferRequestDto): TransferRequestDto

    @GET("transfer-requests/{id}")
    suspend fun getTransferRequest(@Path("id") id: String): TransferRequestDto

    @GET("transfer-requests/seller/{sellerId}")
    suspend fun getSellerRequests(@Path("sellerId") sellerId: String): List<TransferRequestDto>

    @GET("transfer-requests/buyer/{buyerId}")
    suspend fun getBuyerRequests(@Path("buyerId") buyerId: String): List<TransferRequestDto>

    @PUT("transfer-requests/{id}/approve")
    suspend fun approveTransferRequest(
        @Path("id") id: String,
        @Body data: Map<String, String>
    ): TransferRequestDto

    @PUT("transfer-requests/{id}/reject")
    suspend fun rejectTransferRequest(
        @Path("id") id: String,
        @Body data: Map<String, String>
    ): Map<String, String>
}

