package com.example.maslahty.data.mappers

import com.example.maslahty.data.local.entities.ViolationEntity
import com.example.maslahty.data.models.ViolationDto
import com.example.maslahty.domain.entities.Violation
import com.example.maslahty.domain.entities.ViolationStatus
import java.util.Date

fun ViolationDto.toEntity(): ViolationEntity {
    return ViolationEntity(
        id = id,
        vehicleId = vehicleId,
        violationType = violationType,
        description = description,
        date = date,
        amount = amount,
        status = status,
        location = location
    )
}

fun ViolationEntity.toDomain(): Violation {
    return Violation(
        id = id,
        vehicleId = vehicleId,
        violationType = violationType,
        description = description,
        date = Date(date),
        amount = amount,
        status = ViolationStatus.valueOf(status),
        location = location
    )
}

fun ViolationDto.toDomain(): Violation {
    return Violation(
        id = id,
        vehicleId = vehicleId,
        violationType = violationType,
        description = description,
        date = Date(date),
        amount = amount,
        status = ViolationStatus.valueOf(status),
        location = location
    )
}
