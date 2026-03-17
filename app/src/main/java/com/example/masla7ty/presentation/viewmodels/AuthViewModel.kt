package com.example.maslahty.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maslahty.domain.common.Result
import com.example.maslahty.domain.usecases.auth.RegisterUserUseCase
import com.example.maslahty.domain.usecases.auth.SendOTPUseCase
import com.example.maslahty.domain.usecases.auth.VerifyOTPUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sendOTPUseCase: SendOTPUseCase,
    private val verifyOTPUseCase: VerifyOTPUseCase,
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _otpSentState = MutableStateFlow<AuthState>(AuthState.Initial)
    val otpSentState: StateFlow<AuthState> = _otpSentState

    private val _otpVerifyState = MutableStateFlow<AuthState>(AuthState.Initial)
    val otpVerifyState: StateFlow<AuthState> = _otpVerifyState

    fun sendOTP(nationalId: String, phoneNumber: String) {
        viewModelScope.launch {
            _otpSentState.value = AuthState.Loading
            val result = sendOTPUseCase(nationalId, phoneNumber)
            _otpSentState.value = when (result) {
                is Result.Success -> AuthState.OTPSent
                is Result.Error -> AuthState.Error(result.exception.message ?: "Unknown error")
                is Result.Loading -> AuthState.Loading
            }
        }
    }

    fun verifyOTP(phoneNumber: String, code: String) {
        viewModelScope.launch {
            _otpVerifyState.value = AuthState.Loading
            val result = verifyOTPUseCase(phoneNumber, code)
            _otpVerifyState.value = when (result) {
                is Result.Success -> AuthState.LoginSuccess
                is Result.Error -> AuthState.Error(result.exception.message ?: "Unknown error")
                is Result.Loading -> AuthState.Loading
            }
        }
    }

    fun resetState() {
        _otpSentState.value = AuthState.Initial
        _otpVerifyState.value = AuthState.Initial
    }
}

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    object OTPSent : AuthState()
    object LoginSuccess : AuthState()
    data class Error(val message: String) : AuthState()
}

