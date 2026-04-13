package com.example.maslahty.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "violations")
data class ViolationEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "vehicle_id")
    val vehicleId: String,
    @ColumnInfo(name = "violation_type")
    val violationType: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "date")
    val date: Long,
    @ColumnInfo(name = "amount")
    val amount: Double,
    @ColumnInfo(name = "status")
    val status: String,
    @ColumnInfo(name = "location")
    val location: String
)
