package com.example.maslahty.domain.usecases.vehicle

import com.example.maslahty.domain.common.Result
import com.example.maslahty.domain.entities.Vehicle
import com.example.maslahty.domain.repositories.VehicleRepository

class GetUserVehiclesUseCase(private val vehicleRepository: VehicleRepository) {
    suspend operator fun invoke(userId: String): Result<List<Vehicle>> {
        return vehicleRepository.getUserVehicles(userId)
    }
}
