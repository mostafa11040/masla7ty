package com.example.maslahty.domain.utils

object ValidationUtil {
    fun isValidNationalId(nationalId: String): Boolean {
        return nationalId.matches(Regex("^[0-9]{10}$"))
    }

    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber.matches(Regex("^[0-9]{9,15}$"))
    }

    fun isValidEmail(email: String): Boolean {
        return email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)$"))
    }

    fun isValidOTPCode(code: String): Boolean {
        return code.matches(Regex("^[0-9]{4,6}$"))
    }

    fun isValidLicensePlate(plate: String): Boolean {
        // Accept user typing with optional spaces/hyphens then validate canonical pattern.
        val normalized = plate.replace("-", "").replace(" ", "").uppercase()
        return normalized.matches(Regex("^[0-9]{3}[A-Z]{3}[0-9]{4}$"))
    }

    fun isValidChassisNumber(chassisNumber: String): Boolean {
        return chassisNumber.length >= 10 && chassisNumber.matches(Regex("^[A-Z0-9]+$"))
    }

    fun isValidEngineNumber(engineNumber: String): Boolean {
        return engineNumber.length >= 10 && engineNumber.matches(Regex("^[A-Z0-9]+$"))
    }

    fun isValidPrice(price: Double): Boolean {
        return price > 0
    }
}

