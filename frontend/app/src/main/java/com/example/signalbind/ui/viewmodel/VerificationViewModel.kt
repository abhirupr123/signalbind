package com.example.signalbind.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.signalbind.data.model.ConsentReceipt
import com.example.signalbind.data.network.NetworkModule
import com.example.signalbind.data.repository.SignalBindRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class VerificationUiState(
    val isLoading: Boolean = false,
    val mobileNumber: String = "",
    val mockMode: Boolean = false,
    val consentReceipt: ConsentReceipt? = null,
    val errorMessage: String? = null
)

class VerificationViewModel : ViewModel() {
    
    private val repository = SignalBindRepository(NetworkModule.apiService)
    
    private val _uiState = MutableStateFlow(VerificationUiState())
    val uiState: StateFlow<VerificationUiState> = _uiState.asStateFlow()
    
    fun updateMobileNumber(number: String) {
        _uiState.value = _uiState.value.copy(mobileNumber = number)
    }
    
    fun toggleMockMode() {
        _uiState.value = _uiState.value.copy(mockMode = !_uiState.value.mockMode)
    }
    
    fun verifyMobileNumber() {
        val currentState = _uiState.value
        
        if (currentState.mobileNumber.isBlank()) {
            _uiState.value = currentState.copy(errorMessage = "Please enter a mobile number")
            return
        }
        
        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoading = true, errorMessage = null)
            
            repository.verifyMobileNumber(
                mobileNumber = currentState.mobileNumber,
                mockMode = currentState.mockMode
            ).fold(
                onSuccess = { receipt ->
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        consentReceipt = receipt,
                        errorMessage = null
                    )
                },
                onFailure = { error ->
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Unknown error occurred"
                    )
                }
            )
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    fun clearReceipt() {
        _uiState.value = _uiState.value.copy(consentReceipt = null)
    }
}
