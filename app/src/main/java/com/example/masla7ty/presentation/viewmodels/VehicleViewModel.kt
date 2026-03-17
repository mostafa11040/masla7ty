package com.example.maslahty.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maslahty.domain.common.Result
import com.example.maslahty.domain.entities.Vehicle
import com.example.maslahty.domain.usecases.vehicle.AddVehicleUseCase
import com.example.maslahty.domain.usecases.vehicle.GetVehicleByPlateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehicleViewModel @Inject constructor(
    private val addVehicleUseCase: AddVehicleUseCase,
    private val getVehicleByPlateUseCase: GetVehicleByPlateUseCase
) : ViewModel() {

    private val _vehicleState = MutableStateFlow<VehicleState>(VehicleState.Initial)
    val vehicleState: StateFlow<VehicleState> = _vehicleState

    private val _getVehicleState = MutableStateFlow<VehicleState>(VehicleState.Initial)
    val getVehicleState: StateFlow<VehicleState> = _getVehicleState

    fun addVehicle(vehicle: Vehicle) {
        viewModelScope.launch {
            _vehicleState.value = VehicleState.Loading
            val result = addVehicleUseCase(vehicle)
            _vehicleState.value = when (result) {
                is Result.Success -> VehicleState.VehicleAdded(result.data)
                is Result.Error -> VehicleState.Error(result.exception.message ?: "Unknown error")
                is Result.Loading -> VehicleState.Loading
            }
        }
    }

    fun getVehicleByPlate(licensePlate: String) {
        viewModelScope.launch {
            _getVehicleState.value = VehicleState.Loading
            val result = getVehicleByPlateUseCase(licensePlate)
            _getVehicleState.value = when (result) {
                is Result.Success -> VehicleState.VehicleLoaded(result.data)
                is Result.Error -> VehicleState.Error(result.exception.message ?: "Unknown error")
                is Result.Loading -> VehicleState.Loading
            }
        }
    }

    fun resetState() {
        _vehicleState.value = VehicleState.Initial
        _getVehicleState.value = VehicleState.Initial
    }
}

sealed class VehicleState {
    object Initial : VehicleState()
    object Loading : VehicleState()
    data class VehicleLoaded(val vehicle: Vehicle) : VehicleState()
    data class VehicleAdded(val vehicle: Vehicle) : VehicleState()
    data class Error(val message: String) : VehicleState()
}

