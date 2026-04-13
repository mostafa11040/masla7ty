package com.example.maslahty.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.maslahty.data.local.entities.ViolationEntity

@Dao
interface ViolationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertViolation(violation: ViolationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertViolations(violations: List<ViolationEntity>)

    @Query("SELECT * FROM violations WHERE vehicle_id = :vehicleId")
    suspend fun getViolationsByVehicleId(vehicleId: String): List<ViolationEntity>

    @Query("SELECT * FROM violations WHERE id = :id")
    suspend fun getViolationById(id: String): ViolationEntity?

    @Query("SELECT COUNT(*) > 0 FROM violations WHERE vehicle_id = :vehicleId AND status = 'UNPAID'")
    suspend fun hasUnpaidViolations(vehicleId: String): Boolean
}
