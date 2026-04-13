package com.example.maslahty.data.repositories

import com.example.maslahty.data.local.dao.ViolationDao
import com.example.maslahty.data.mappers.toDomain
import com.example.maslahty.data.mappers.toEntity
import com.example.maslahty.data.remote.ApiService
import com.example.maslahty.domain.common.NetworkException
import com.example.maslahty.domain.common.Result
import com.example.maslahty.domain.entities.Violation
import com.example.maslahty.domain.repositories.ViolationRepository

class ViolationRepositoryImpl(
    private val apiService: ApiService,
    private val violationDao: ViolationDao
) : ViolationRepository {

    override suspend fun getVehicleViolations(vehicleId: String): Result<List<Violation>> {
        return try {
            val violationDtos = apiService.getVehicleViolations(vehicleId)
            val violations = violationDtos.map {
                violationDao.insertViolation(it.toEntity())
                it.toDomain()
            }
            Result.Success(violations)
        } catch (e: Exception) {
            // Fallback to local cache
            try {
                val cached = violationDao.getViolationsByVehicleId(vehicleId)
                if (cached.isNotEmpty()) {
                    Result.Success(cached.map { it.toDomain() })
                } else {
                    Result.Error(NetworkException("فشل في تحميل المخالفات: ${e.message}"))
                }
            } catch (dbError: Exception) {
                Result.Error(NetworkException("فشل في تحميل المخالفات: ${e.message}"))
            }
        }
    }

    override suspend fun getViolationById(id: String): Result<Violation> {
        return try {
            val cached = violationDao.getViolationById(id)
            if (cached != null) {
                Result.Success(cached.toDomain())
            } else {
                val dto = apiService.getViolationById(id)
                violationDao.insertViolation(dto.toEntity())
                Result.Success(dto.toDomain())
            }
        } catch (e: Exception) {
            Result.Error(NetworkException("فشل في تحميل المخالفة: ${e.message}"))
        }
    }

    override suspend fun hasUnpaidViolations(vehicleId: String): Result<Boolean> {
        return try {
            val violations = apiService.getVehicleViolations(vehicleId)
            violations.forEach { violationDao.insertViolation(it.toEntity()) }
            val hasUnpaid = violations.any { it.status == "UNPAID" }
            Result.Success(hasUnpaid)
        } catch (e: Exception) {
            try {
                val hasUnpaid = violationDao.hasUnpaidViolations(vehicleId)
                Result.Success(hasUnpaid)
            } catch (dbError: Exception) {
                Result.Error(NetworkException("فشل في التحقق من المخالفات: ${e.message}"))
            }
        }
    }
}
