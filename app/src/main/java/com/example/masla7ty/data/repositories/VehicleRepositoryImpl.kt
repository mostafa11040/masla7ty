package com.example.maslahty.data.repositories

import com.example.maslahty.data.local.dao.VehicleDao
import com.example.maslahty.data.mappers.toDomain
import com.example.maslahty.data.mappers.toEntity
import com.example.maslahty.data.models.VehicleDto
import com.example.maslahty.data.remote.ApiService
import com.example.maslahty.domain.common.NetworkException
import com.example.maslahty.domain.common.Result
import com.example.maslahty.domain.entities.Vehicle
import com.example.maslahty.domain.repositories.VehicleRepository

class VehicleRepositoryImpl(
    private val apiService: ApiService,
    private val vehicleDao: VehicleDao
) : VehicleRepository {

    override suspend fun getVehicleByPlate(licensePlate: String): Result<Vehicle> {
        return try {
            val vehicleEntity = vehicleDao.getVehicleByPlate(licensePlate)
            if (vehicleEntity != null) {
                Result.Success(vehicleEntity.toDomain())
            } else {
                val vehicleDto = apiService.getVehicleByPlate(licensePlate)
                vehicleDao.insertVehicle(vehicleDto.toEntity())
                Result.Success(vehicleDto.toDomain())
            }
        } catch (e: Exception) {
            Result.Error(NetworkException("Failed to get vehicle: ${e.message}"))
        }
    }

    override suspend fun getVehicleById(id: String): Result<Vehicle> {
        return try {
            val vehicleEntity = vehicleDao.getVehicleById(id)
            if (vehicleEntity != null) {
                Result.Success(vehicleEntity.toDomain())
            } else {
                val vehicleDto = apiService.getVehicle(id)
                vehicleDao.insertVehicle(vehicleDto.toEntity())
                Result.Success(vehicleDto.toDomain())
            }
        } catch (e: Exception) {
            Result.Error(NetworkException("Failed to get vehicle: ${e.message}"))
        }
    }

    override suspend fun createVehicle(vehicle: Vehicle): Result<Vehicle> {
        return try {
            val vehicleEntity = vehicle.toEntity()
            val vehicleDto = VehicleDto(
                vehicleEntity.id, vehicleEntity.ownerId, vehicleEntity.licensePlate,
                vehicleEntity.chassisNumber, vehicleEntity.engineNumber, vehicleEntity.model,
                vehicleEntity.manufacturingYear, vehicleEntity.color, vehicleEntity.kilometers,
                vehicleEntity.condition, vehicleEntity.licenseImageUrl, vehicleEntity.vehicleImageUrl,
                vehicleEntity.chassisImageUrl, vehicleEntity.engineImageUrl,
                vehicleEntity.createdAt, vehicleEntity.updatedAt
            )
            val response = apiService.createVehicle(vehicleDto)
            vehicleDao.insertVehicle(response.toEntity())
            Result.Success(response.toDomain())
        } catch (e: Exception) {
            Result.Error(NetworkException("Failed to create vehicle: ${e.message}"))
        }
    }

    override suspend fun updateVehicle(vehicle: Vehicle): Result<Vehicle> {
        return try {
            val vehicleEntity = vehicle.toEntity()
            val vehicleDto = VehicleDto(
                vehicleEntity.id, vehicleEntity.ownerId, vehicleEntity.licensePlate,
                vehicleEntity.chassisNumber, vehicleEntity.engineNumber, vehicleEntity.model,
                vehicleEntity.manufacturingYear, vehicleEntity.color, vehicleEntity.kilometers,
                vehicleEntity.condition, vehicleEntity.licenseImageUrl, vehicleEntity.vehicleImageUrl,
                vehicleEntity.chassisImageUrl, vehicleEntity.engineImageUrl,
                vehicleEntity.createdAt, vehicleEntity.updatedAt
            )
            val response = apiService.updateVehicle(vehicle.id, vehicleDto)
            vehicleDao.updateVehicle(response.toEntity())
            Result.Success(response.toDomain())
        } catch (e: Exception) {
            Result.Error(NetworkException("Failed to update vehicle: ${e.message}"))
        }
    }

    override suspend fun getUserVehicles(userId: String): Result<List<Vehicle>> {
        return try {
            val vehicleDtos = apiService.getUserVehicles(userId)
            val vehicles = vehicleDtos.map {
                vehicleDao.insertVehicle(it.toEntity())
                it.toDomain()
            }
            Result.Success(vehicles)
        } catch (e: Exception) {
            Result.Error(NetworkException("Failed to get vehicles: ${e.message}"))
        }
    }

    override suspend fun deleteVehicle(id: String): Result<Unit> {
        return try {
            val vehicle = vehicleDao.getVehicleById(id) ?: return Result.Error(
                NetworkException("Vehicle not found")
            )
            vehicleDao.deleteVehicle(vehicle)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(NetworkException("Failed to delete vehicle: ${e.message}"))
        }
    }
}

