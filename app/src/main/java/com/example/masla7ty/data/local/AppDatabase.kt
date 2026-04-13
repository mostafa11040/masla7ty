package com.example.maslahty.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.maslahty.data.local.dao.TransferRequestDao
import com.example.maslahty.data.local.dao.UserDao
import com.example.maslahty.data.local.dao.VehicleDao
import com.example.maslahty.data.local.dao.ViolationDao
import com.example.maslahty.data.local.entities.TransferRequestEntity
import com.example.maslahty.data.local.entities.UserEntity
import com.example.maslahty.data.local.entities.VehicleEntity
import com.example.maslahty.data.local.entities.ViolationEntity

@Database(
    entities = [
        UserEntity::class,
        VehicleEntity::class,
        TransferRequestEntity::class,
        ViolationEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun vehicleDao(): VehicleDao
    abstract fun transferRequestDao(): TransferRequestDao
    abstract fun violationDao(): ViolationDao

    companion object {
        const val DATABASE_NAME = "maslahty_database"
    }
}

