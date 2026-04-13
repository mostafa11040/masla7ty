package com.example.maslahty.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maslahty.domain.common.Result
import com.example.maslahty.domain.entities.Vehicle
import com.example.maslahty.domain.entities.Violation
import com.example.maslahty.domain.usecases.vehicle.GetUserVehiclesUseCase
import com.example.maslahty.domain.usecases.violation.CheckViolationsForTransferUseCase
import com.example.maslahty.domain.usecases.violation.GetVehicleViolationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViolationsViewModel @Inject constructor(
    private val getUserVehiclesUseCase: GetUserVehiclesUseCase,
    private val getVehicleViolationsUseCase: GetVehicleViolationsUseCase,
    private val checkViolationsForTransferUseCase: CheckViolationsForTransferUseCase
) : ViewModel() {

    private val _vehiclesState = MutableStateFlow<ViolationsUiState>(ViolationsUiState.Initial)
    val vehiclesState: StateFlow<ViolationsUiState> = _vehiclesState

    private val _violationsState = MutableStateFlow<ViolationsUiState>(ViolationsUiState.Initial)
    val violationsState: StateFlow<ViolationsUiState> = _violationsState

    private val _transferCheckState = MutableStateFlow<TransferCheckState>(TransferCheckState.Initial)
    val transferCheckState: StateFlow<TransferCheckState> = _transferCheckState

    fun loadUserVehicles(userId: String = "user1") {
        viewModelScope.launch {
            _vehiclesState.value = ViolationsUiState.Loading
            val result = getUserVehiclesUseCase(userId)
            _vehiclesState.value = when (result) {
                is Result.Success -> ViolationsUiState.VehiclesLoaded(result.data)
                is Result.Error -> ViolationsUiState.Error(result.exception.message ?: "خطأ غير معروف")
                is Result.Loading -> ViolationsUiState.Loading
            }
        }
    }

    fun loadVehicleViolations(vehicleId: String) {
        viewModelScope.launch {
            _violationsState.value = ViolationsUiState.Loading
            val result = getVehicleViolationsUseCase(vehicleId)
            _violationsState.value = when (result) {
                is Result.Success -> ViolationsUiState.ViolationsLoaded(result.data)
                is Result.Error -> ViolationsUiState.Error(result.exception.message ?: "خطأ غير معروف")
                is Result.Loading -> ViolationsUiState.Loading
            }
        }
    }

    fun checkViolationsForTransfer(vehicleId: String) {
        viewModelScope.launch {
            _transferCheckState.value = TransferCheckState.Checking
            val result = checkViolationsForTransferUseCase(vehicleId)
            _transferCheckState.value = when (result) {
                is Result.Success -> {
                    if (result.data) TransferCheckState.HasViolations
                    else TransferCheckState.Clear
                }
                is Result.Error -> TransferCheckState.Error(result.exception.message ?: "خطأ غير معروف")
                is Result.Loading -> TransferCheckState.Checking
            }
        }
    }

    fun resetViolationsState() {
        _violationsState.value = ViolationsUiState.Initial
    }
}

sealed class ViolationsUiState {
    object Initial : ViolationsUiState()
    object Loading : ViolationsUiState()
    data class VehiclesLoaded(val vehicles: List<Vehicle>) : ViolationsUiState()
    data class ViolationsLoaded(val violations: List<Violation>) : ViolationsUiState()
    data class Error(val message: String) : ViolationsUiState()
}

sealed class TransferCheckState {
    object Initial : TransferCheckState()
    object Checking : TransferCheckState()
    object Clear : TransferCheckState()
    object HasViolations : TransferCheckState()
    data class Error(val message: String) : TransferCheckState()
}
