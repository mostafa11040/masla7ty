package com.example.maslahty.domain.repositories

import com.example.maslahty.domain.common.Result
import com.example.maslahty.domain.entities.Violation

interface ViolationRepository {
    suspend fun getVehicleViolations(vehicleId: String): Result<List<Violation>>
    suspend fun getViolationById(id: String): Result<Violation>
    suspend fun hasUnpaidViolations(vehicleId: String): Result<Boolean>
}
