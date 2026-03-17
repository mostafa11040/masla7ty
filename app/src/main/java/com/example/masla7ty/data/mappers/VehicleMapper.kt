package com.example.maslahty.data.mappers

import com.example.maslahty.data.local.entities.VehicleEntity
import com.example.maslahty.data.models.VehicleDto
import com.example.maslahty.domain.entities.Vehicle
import com.example.maslahty.domain.entities.VehicleCondition
import java.util.Date

fun VehicleDto.toEntity(): VehicleEntity {
    return VehicleEntity(
        id = id,
        ownerId = ownerId,
        licensePlate = licensePlate,
        chassisNumber = chassisNumber,
        engineNumber = engineNumber,
        model = model,
        manufacturingYear = manufacturingYear,
        color = color,
        kilometers = kilometers,
        condition = condition,
        licenseImageUrl = licenseImageUrl,
        vehicleImageUrl = vehicleImageUrl,
        chassisImageUrl = chassisImageUrl,
        engineImageUrl = engineImageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun VehicleEntity.toDomain(): Vehicle {
    return Vehicle(
        id = id,
        ownerId = ownerId,
        licensePlate = licensePlate,
        chassisNumber = chassisNumber,
        engineNumber = engineNumber,
        model = model,
        manufacturingYear = manufacturingYear,
        color = color,
        kilometers = kilometers,
        condition = VehicleCondition.valueOf(condition),
        licenseImageUrl = licenseImageUrl,
        vehicleImageUrl = vehicleImageUrl,
        chassisImageUrl = chassisImageUrl,
        engineImageUrl = engineImageUrl,
        createdAt = Date(createdAt),
        updatedAt = Date(updatedAt)
    )
}

fun Vehicle.toEntity(): VehicleEntity {
    return VehicleEntity(
        id = id,
        ownerId = ownerId,
        licensePlate = licensePlate,
        chassisNumber = chassisNumber,
        engineNumber = engineNumber,
        model = model,
        manufacturingYear = manufacturingYear,
        color = color,
        kilometers = kilometers,
        condition = condition.name,
        licenseImageUrl = licenseImageUrl,
        vehicleImageUrl = vehicleImageUrl,
        chassisImageUrl = chassisImageUrl,
        engineImageUrl = engineImageUrl,
        createdAt = createdAt.time,
        updatedAt = updatedAt.time
    )
}

fun VehicleDto.toDomain(): Vehicle {
    return Vehicle(
        id = id,
        ownerId = ownerId,
        licensePlate = licensePlate,
        chassisNumber = chassisNumber,
        engineNumber = engineNumber,
        model = model,
        manufacturingYear = manufacturingYear,
        color = color,
        kilometers = kilometers,
        condition = VehicleCondition.valueOf(condition),
        licenseImageUrl = licenseImageUrl,
        vehicleImageUrl = vehicleImageUrl,
        chassisImageUrl = chassisImageUrl,
        engineImageUrl = engineImageUrl,
        createdAt = Date(createdAt),
        updatedAt = Date(updatedAt)
    )
}

