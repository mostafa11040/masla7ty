package com.example.maslahty.data.models

import com.google.gson.annotations.SerializedName

data class VehicleDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("owner_id")
    val ownerId: String,
    @SerializedName("license_plate")
    val licensePlate: String,
    @SerializedName("chassis_number")
    val chassisNumber: String,
    @SerializedName("engine_number")
    val engineNumber: String,
    @SerializedName("model")
    val model: String,
    @SerializedName("manufacturing_year")
    val manufacturingYear: Int,
    @SerializedName("color")
    val color: String,
    @SerializedName("kilometers")
    val kilometers: Int,
    @SerializedName("condition")
    val condition: String,
    @SerializedName("license_image_url")
    val licenseImageUrl: String?,
    @SerializedName("vehicle_image_url")
    val vehicleImageUrl: String?,
    @SerializedName("chassis_image_url")
    val chassisImageUrl: String?,
    @SerializedName("engine_image_url")
    val engineImageUrl: String?,
    @SerializedName("created_at")
    val createdAt: Long,
    @SerializedName("updated_at")
    val updatedAt: Long
)

