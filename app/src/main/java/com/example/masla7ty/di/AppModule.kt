package com.example.maslahty.di

import android.content.Context
import androidx.room.Room
import com.example.maslahty.data.local.AppDatabase
import com.example.maslahty.data.local.dao.TransferRequestDao
import com.example.maslahty.data.local.dao.UserDao
import com.example.maslahty.data.local.dao.VehicleDao
import com.example.maslahty.data.local.dao.ViolationDao
import com.example.maslahty.data.remote.ApiService
import com.example.maslahty.data.remote.FakeApiService
import com.example.maslahty.data.repositories.AuthRepositoryImpl
import com.example.maslahty.data.repositories.TransferRequestRepositoryImpl
import com.example.maslahty.data.repositories.UserRepositoryImpl
import com.example.maslahty.data.repositories.VehicleRepositoryImpl
import com.example.maslahty.data.repositories.ViolationRepositoryImpl
import com.example.maslahty.domain.repositories.AuthRepository
import com.example.maslahty.domain.repositories.TransferRequestRepository
import com.example.maslahty.domain.repositories.UserRepository
import com.example.maslahty.domain.repositories.VehicleRepository
import com.example.maslahty.domain.repositories.ViolationRepository
import com.example.maslahty.domain.usecases.auth.RegisterUserUseCase
import com.example.maslahty.domain.usecases.auth.SendOTPUseCase
import com.example.maslahty.domain.usecases.auth.VerifyOTPUseCase
import com.example.maslahty.domain.usecases.transfer.ApproveTransferRequestUseCase
import com.example.maslahty.domain.usecases.transfer.CreateTransferRequestUseCase
import com.example.maslahty.domain.usecases.transfer.GetBuyerRequestsUseCase
import com.example.maslahty.domain.usecases.vehicle.AddVehicleUseCase
import com.example.maslahty.domain.usecases.vehicle.GetUserVehiclesUseCase
import com.example.maslahty.domain.usecases.vehicle.GetVehicleByPlateUseCase
import com.example.maslahty.domain.usecases.violation.CheckViolationsForTransferUseCase
import com.example.maslahty.domain.usecases.violation.GetVehicleViolationsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://api.example.com/"  // Replace with actual backend URL

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

    @Provides
    @Singleton
    fun provideVehicleDao(database: AppDatabase): VehicleDao = database.vehicleDao()

    @Provides
    @Singleton
    fun provideTransferRequestDao(database: AppDatabase): TransferRequestDao = database.transferRequestDao()

    @Provides
    @Singleton
    fun provideViolationDao(database: AppDatabase): ViolationDao = database.violationDao()

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return FakeApiService()
    }

    // Repositories
    @Provides
    @Singleton
    fun provideUserRepository(
        apiService: ApiService,
        userDao: UserDao
    ): UserRepository {
        return UserRepositoryImpl(apiService, userDao)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        apiService: ApiService,
        userDao: UserDao,
        @ApplicationContext context: Context
    ): AuthRepository {
        return AuthRepositoryImpl(apiService, userDao, context)
    }

    @Provides
    @Singleton
    fun provideVehicleRepository(
        apiService: ApiService,
        vehicleDao: VehicleDao
    ): VehicleRepository {
        return VehicleRepositoryImpl(apiService, vehicleDao)
    }

    @Provides
    @Singleton
    fun provideTransferRequestRepository(
        apiService: ApiService,
        transferRequestDao: TransferRequestDao
    ): TransferRequestRepository {
        return TransferRequestRepositoryImpl(apiService, transferRequestDao)
    }

    @Provides
    @Singleton
    fun provideViolationRepository(
        apiService: ApiService,
        violationDao: ViolationDao
    ): ViolationRepository {
        return ViolationRepositoryImpl(apiService, violationDao)
    }

    // Use Cases
    @Provides
    @Singleton
    fun provideSendOTPUseCase(authRepository: AuthRepository): SendOTPUseCase {
        return SendOTPUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideVerifyOTPUseCase(authRepository: AuthRepository): VerifyOTPUseCase {
        return VerifyOTPUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideRegisterUserUseCase(authRepository: AuthRepository): RegisterUserUseCase {
        return RegisterUserUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideAddVehicleUseCase(vehicleRepository: VehicleRepository): AddVehicleUseCase {
        return AddVehicleUseCase(vehicleRepository)
    }

    @Provides
    @Singleton
    fun provideGetVehicleByPlateUseCase(vehicleRepository: VehicleRepository): GetVehicleByPlateUseCase {
        return GetVehicleByPlateUseCase(vehicleRepository)
    }

    @Provides
    @Singleton
    fun provideGetUserVehiclesUseCase(vehicleRepository: VehicleRepository): GetUserVehiclesUseCase {
        return GetUserVehiclesUseCase(vehicleRepository)
    }

    @Provides
    @Singleton
    fun provideCreateTransferRequestUseCase(transferRepository: TransferRequestRepository): CreateTransferRequestUseCase {
        return CreateTransferRequestUseCase(transferRepository)
    }

    @Provides
    @Singleton
    fun provideGetBuyerRequestsUseCase(transferRepository: TransferRequestRepository): GetBuyerRequestsUseCase {
        return GetBuyerRequestsUseCase(transferRepository)
    }

    @Provides
    @Singleton
    fun provideApproveTransferRequestUseCase(transferRepository: TransferRequestRepository): ApproveTransferRequestUseCase {
        return ApproveTransferRequestUseCase(transferRepository)
    }

    @Provides
    @Singleton
    fun provideGetVehicleViolationsUseCase(violationRepository: ViolationRepository): GetVehicleViolationsUseCase {
        return GetVehicleViolationsUseCase(violationRepository)
    }

    @Provides
    @Singleton
    fun provideCheckViolationsForTransferUseCase(violationRepository: ViolationRepository): CheckViolationsForTransferUseCase {
        return CheckViolationsForTransferUseCase(violationRepository)
    }
}


