package com.example.maslahty.presentation.navigation

sealed class Screen(val route: String) {
    object LoginScreen : Screen("login")
    object OTPVerificationScreen : Screen("otp_verification/{phoneNumber}") {
        fun createRoute(phoneNumber: String) = "otp_verification/$phoneNumber"
    }
    object RegistrationScreen : Screen("registration")
    object HomeScreen : Screen("home")
    object VehicleDetailsScreen : Screen("vehicle_details")
    object ImageUploadScreen : Screen("image_upload/{vehicleId}") {
        fun createRoute(vehicleId: String) = "image_upload/$vehicleId"
    }
    object PricingScreen : Screen("pricing/{vehicleId}") {
        fun createRoute(vehicleId: String) = "pricing/$vehicleId"
    }
    object TransferRequestScreen : Screen("transfer_request/{vehicleId}") {
        fun createRoute(vehicleId: String) = "transfer_request/$vehicleId"
    }
    object RequestsManagementScreen : Screen("requests_management")
    object ApprovalScreen : Screen("approval/{requestId}") {
        fun createRoute(requestId: String) = "approval/$requestId"
    }
    object RequestDetailsScreen : Screen("request_details/{requestId}") {
        fun createRoute(requestId: String) = "request_details/$requestId"
    }
}

