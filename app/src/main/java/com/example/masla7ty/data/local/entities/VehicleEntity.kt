package com.example.maslahty.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class VehicleEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "owner_id")
    val ownerId: String,
    @ColumnInfo(name = "license_plate")
    val licensePlate: String,
    @ColumnInfo(name = "chassis_number")
    val chassisNumber: String,
    @ColumnInfo(name = "engine_number")
    val engineNumber: String,
    @ColumnInfo(name = "model")
    val model: String,
    @ColumnInfo(name = "manufacturing_year")
    val manufacturingYear: Int,
    @ColumnInfo(name = "color")
    val color: String,
    @ColumnInfo(name = "kilometers")
    val kilometers: Int,
    @ColumnInfo(name = "condition")
    val condition: String,
    @ColumnInfo(name = "license_image_url")
    val licenseImageUrl: String?,
    @ColumnInfo(name = "vehicle_image_url")
    val vehicleImageUrl: String?,
    @ColumnInfo(name = "chassis_image_url")
    val chassisImageUrl: String?,
    @ColumnInfo(name = "engine_image_url")
    val engineImageUrl: String?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)

