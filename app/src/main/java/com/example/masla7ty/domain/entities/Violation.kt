package com.example.maslahty.domain.entities

import java.util.Date

data class Violation(
    val id: String,
    val vehicleId: String,
    val violationType: String,
    val description: String,
    val date: Date,
    val amount: Double,
    val status: ViolationStatus,
    val location: String
)

enum class ViolationStatus {
    PAID, UNPAID
}
