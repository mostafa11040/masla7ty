package com.example.maslahty.domain.repositories

import com.example.maslahty.domain.common.Result
import com.example.maslahty.domain.entities.Vehicle

interface VehicleRepository {
    suspend fun getVehicleByPlate(licensePlate: String): Result<Vehicle>
    suspend fun getVehicleById(id: String): Result<Vehicle>
    suspend fun createVehicle(vehicle: Vehicle): Result<Vehicle>
    suspend fun updateVehicle(vehicle: Vehicle): Result<Vehicle>
    suspend fun getUserVehicles(userId: String): Result<List<Vehicle>>
    suspend fun deleteVehicle(id: String): Result<Unit>
}

