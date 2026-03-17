package com.example.maslahty.domain.entities

import java.util.Date

data class Vehicle(
    val id: String,
    val ownerId: String,
    val licensePlate: String,
    val chassisNumber: String,
    val engineNumber: String,
    val model: String,
    val manufacturingYear: Int,
    val color: String,
    val kilometers: Int,
    val condition: VehicleCondition,
    val licenseImageUrl: String?,
    val vehicleImageUrl: String?,
    val chassisImageUrl: String?,
    val engineImageUrl: String?,
    val createdAt: Date,
    val updatedAt: Date
)

enum class VehicleCondition {
    EXCELLENT, VERY_GOOD, GOOD, FAIR, POOR
}

