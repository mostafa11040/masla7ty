package com.example.maslahty.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.maslahty.data.local.entities.VehicleEntity

@Dao
interface VehicleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicle(vehicle: VehicleEntity)

    @Update
    suspend fun updateVehicle(vehicle: VehicleEntity)

    @Delete
    suspend fun deleteVehicle(vehicle: VehicleEntity)

    @Query("SELECT * FROM vehicles WHERE id = :id")
    suspend fun getVehicleById(id: String): VehicleEntity?

    @Query("SELECT * FROM vehicles WHERE license_plate = :licensePlate")
    suspend fun getVehicleByPlate(licensePlate: String): VehicleEntity?

    @Query("SELECT * FROM vehicles WHERE owner_id = :ownerId")
    suspend fun getUserVehicles(ownerId: String): List<VehicleEntity>

    @Query("SELECT * FROM vehicles")
    suspend fun getAllVehicles(): List<VehicleEntity>
}

