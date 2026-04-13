package com.example.maslahty.data.models

import com.google.gson.annotations.SerializedName

data class ViolationDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("vehicle_id")
    val vehicleId: String,
    @SerializedName("violation_type")
    val violationType: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("date")
    val date: Long,
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("status")
    val status: String,
    @SerializedName("location")
    val location: String
)
