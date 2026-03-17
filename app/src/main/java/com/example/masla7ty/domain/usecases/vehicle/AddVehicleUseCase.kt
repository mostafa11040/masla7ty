package com.example.maslahty.domain.usecases.vehicle

import com.example.maslahty.domain.common.Result
import com.example.maslahty.domain.entities.Vehicle
import com.example.maslahty.domain.repositories.VehicleRepository

class AddVehicleUseCase(private val vehicleRepository: VehicleRepository) {
    suspend operator fun invoke(vehicle: Vehicle): Result<Vehicle> {
        return vehicleRepository.createVehicle(vehicle)
    }
}

