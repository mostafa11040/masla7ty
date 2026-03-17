package com.example.maslahty.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maslahty.domain.common.Result
import com.example.maslahty.domain.entities.TransferRequest
import com.example.maslahty.domain.usecases.transfer.ApproveTransferRequestUseCase
import com.example.maslahty.domain.usecases.transfer.CreateTransferRequestUseCase
import com.example.maslahty.domain.usecases.transfer.GetBuyerRequestsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransferRequestViewModel @Inject constructor(
    private val createTransferRequestUseCase: CreateTransferRequestUseCase,
    private val getBuyerRequestsUseCase: GetBuyerRequestsUseCase,
    private val approveTransferRequestUseCase: ApproveTransferRequestUseCase
) : ViewModel() {

    private val _createRequestState = MutableStateFlow<TransferState>(TransferState.Initial)
    val createRequestState: StateFlow<TransferState> = _createRequestState

    private val _buyerRequestsState = MutableStateFlow<TransferState>(TransferState.Initial)
    val buyerRequestsState: StateFlow<TransferState> = _buyerRequestsState

    private val _approveRequestState = MutableStateFlow<TransferState>(TransferState.Initial)
    val approveRequestState: StateFlow<TransferState> = _approveRequestState

    fun createTransferRequest(request: TransferRequest) {
        viewModelScope.launch {
            _createRequestState.value = TransferState.Loading
            val result = createTransferRequestUseCase(request)
            _createRequestState.value = when (result) {
                is Result.Success -> TransferState.RequestCreated(result.data)
                is Result.Error -> TransferState.Error(result.exception.message ?: "Unknown error")
                is Result.Loading -> TransferState.Loading
            }
        }
    }

    fun getBuyerRequests(buyerId: String) {
        viewModelScope.launch {
            _buyerRequestsState.value = TransferState.Loading
            val result = getBuyerRequestsUseCase(buyerId)
            _buyerRequestsState.value = when (result) {
                is Result.Success -> TransferState.RequestsLoaded(result.data)
                is Result.Error -> TransferState.Error(result.exception.message ?: "Unknown error")
                is Result.Loading -> TransferState.Loading
            }
        }
    }

    fun approveRequest(requestId: String, buyerId: String) {
        viewModelScope.launch {
            _approveRequestState.value = TransferState.Loading
            val result = approveTransferRequestUseCase(requestId, buyerId)
            _approveRequestState.value = when (result) {
                is Result.Success -> TransferState.RequestApproved(result.data)
                is Result.Error -> TransferState.Error(result.exception.message ?: "Unknown error")
                is Result.Loading -> TransferState.Loading
            }
        }
    }

    fun resetState() {
        _createRequestState.value = TransferState.Initial
        _buyerRequestsState.value = TransferState.Initial
        _approveRequestState.value = TransferState.Initial
    }
}

sealed class TransferState {
    object Initial : TransferState()
    object Loading : TransferState()
    data class RequestsLoaded(val requests: List<TransferRequest>) : TransferState()
    data class RequestCreated(val request: TransferRequest) : TransferState()
    data class RequestApproved(val request: TransferRequest) : TransferState()
    data class Error(val message: String) : TransferState()
}

