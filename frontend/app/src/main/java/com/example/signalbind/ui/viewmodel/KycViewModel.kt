package com.example.signalbind.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.signalbind.data.model.KycRequest
import com.example.signalbind.data.model.KycResponse
import com.example.signalbind.data.network.NetworkModule
import com.example.signalbind.data.repository.SignalBindRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class KycFormState(
    // Required fields
    val phoneNumber: String = "",
    val idDocument: String = "",
    val name: String = "",
    val givenName: String = "",
    val familyName: String = "",
    val birthdate: String = "",
    val gender: String = "",
    val email: String = "",
    
    // Optional fields
    val middleNames: String = "",
    val familyNameAtBirth: String = "",
    val nameKanaHankaku: String = "",
    val nameKanaZenkaku: String = "",
    val address: String = "",
    val streetName: String = "",
    val streetNumber: String = "",
    val houseNumberExtension: String = "",
    val postalCode: String = "",
    val region: String = "",
    val locality: String = "",
    val country: String = "",
    
    // UI state
    val isLoading: Boolean = false,
    val kycResponse: KycResponse? = null,
    val errorMessage: String? = null,
    val fieldErrors: Map<String, String> = emptyMap()
)

class KycViewModel : ViewModel() {
    
    private val repository = SignalBindRepository(NetworkModule.apiService)
    
    private val _uiState = MutableStateFlow(KycFormState())
    val uiState: StateFlow<KycFormState> = _uiState.asStateFlow()
    
    fun updatePhoneNumber(value: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = value)
        clearFieldError("phoneNumber")
    }
    
    fun updateIdDocument(value: String) {
        _uiState.value = _uiState.value.copy(idDocument = value)
        clearFieldError("idDocument")
    }
    
    fun updateName(value: String) {
        _uiState.value = _uiState.value.copy(name = value)
        clearFieldError("name")
    }
    
    fun updateGivenName(value: String) {
        _uiState.value = _uiState.value.copy(givenName = value)
        clearFieldError("givenName")
    }
    
    fun updateFamilyName(value: String) {
        _uiState.value = _uiState.value.copy(familyName = value)
        clearFieldError("familyName")
    }
    
    fun updateMiddleNames(value: String) {
        _uiState.value = _uiState.value.copy(middleNames = value)
    }
    
    fun updateFamilyNameAtBirth(value: String) {
        _uiState.value = _uiState.value.copy(familyNameAtBirth = value)
    }
    
    fun updateNameKanaHankaku(value: String) {
        _uiState.value = _uiState.value.copy(nameKanaHankaku = value)
    }
    
    fun updateNameKanaZenkaku(value: String) {
        _uiState.value = _uiState.value.copy(nameKanaZenkaku = value)
    }
    
    fun updateBirthdate(date: Date) {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        _uiState.value = _uiState.value.copy(birthdate = formatter.format(date))
        clearFieldError("birthdate")
    }
    
    fun updateGender(value: String) {
        _uiState.value = _uiState.value.copy(gender = value)
        clearFieldError("gender")
    }
    
    fun updateEmail(value: String) {
        _uiState.value = _uiState.value.copy(email = value)
        clearFieldError("email")
    }
    
    fun updateAddress(value: String) {
        _uiState.value = _uiState.value.copy(address = value)
    }
    
    fun updateStreetName(value: String) {
        _uiState.value = _uiState.value.copy(streetName = value)
    }
    
    fun updateStreetNumber(value: String) {
        _uiState.value = _uiState.value.copy(streetNumber = value)
    }
    
    fun updateHouseNumberExtension(value: String) {
        _uiState.value = _uiState.value.copy(houseNumberExtension = value)
    }
    
    fun updatePostalCode(value: String) {
        _uiState.value = _uiState.value.copy(postalCode = value)
    }
    
    fun updateRegion(value: String) {
        _uiState.value = _uiState.value.copy(region = value)
    }
    
    fun updateLocality(value: String) {
        _uiState.value = _uiState.value.copy(locality = value)
    }
    
    fun updateCountry(value: String) {
        _uiState.value = _uiState.value.copy(country = value)
    }
    
    private fun clearFieldError(fieldName: String) {
        val currentErrors = _uiState.value.fieldErrors.toMutableMap()
        currentErrors.remove(fieldName)
        _uiState.value = _uiState.value.copy(fieldErrors = currentErrors)
    }
    
    fun submitKycForm() {
        val currentState = _uiState.value
        
        // Validate required fields
        val errors = mutableMapOf<String, String>()
        
        if (currentState.phoneNumber.isBlank()) {
            errors["phoneNumber"] = "Phone number is required"
        }
        
        if (currentState.idDocument.isBlank()) {
            errors["idDocument"] = "ID document is required"
        }
        
        if (currentState.name.isBlank()) {
            errors["name"] = "Name is required"
        }
        
        if (currentState.givenName.isBlank()) {
            errors["givenName"] = "Given name is required"
        }
        
        if (currentState.familyName.isBlank()) {
            errors["familyName"] = "Family name is required"
        }
        
        if (currentState.birthdate.isBlank()) {
            errors["birthdate"] = "Birthdate is required"
        }
        
        if (currentState.gender.isBlank()) {
            errors["gender"] = "Gender is required"
        }
        
        if (currentState.email.isBlank()) {
            errors["email"] = "Email is required"
        } else if (!isValidEmail(currentState.email)) {
            errors["email"] = "Please enter a valid email address"
        }
        
        if (errors.isNotEmpty()) {
            _uiState.value = currentState.copy(fieldErrors = errors)
            return
        }
        
        // Create KYC request
        val kycRequest = KycRequest(
            phoneNumber = currentState.phoneNumber,
            idDocument = currentState.idDocument,
            name = currentState.name,
            givenName = currentState.givenName,
            familyName = currentState.familyName,
            middleNames = currentState.middleNames.takeIf { it.isNotBlank() },
            familyNameAtBirth = currentState.familyNameAtBirth.takeIf { it.isNotBlank() },
            nameKanaHankaku = currentState.nameKanaHankaku.takeIf { it.isNotBlank() },
            nameKanaZenkaku = currentState.nameKanaZenkaku.takeIf { it.isNotBlank() },
            birthdate = currentState.birthdate,
            gender = currentState.gender,
            email = currentState.email,
            address = currentState.address.takeIf { it.isNotBlank() },
            streetName = currentState.streetName.takeIf { it.isNotBlank() },
            streetNumber = currentState.streetNumber.takeIf { it.isNotBlank() },
            houseNumberExtension = currentState.houseNumberExtension.takeIf { it.isNotBlank() },
            postalCode = currentState.postalCode.takeIf { it.isNotBlank() },
            region = currentState.region.takeIf { it.isNotBlank() },
            locality = currentState.locality.takeIf { it.isNotBlank() },
            country = currentState.country.takeIf { it.isNotBlank() }
        )
        
        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoading = true, errorMessage = null)
            
            repository.submitKycForm(kycRequest).fold(
                onSuccess = { response ->
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        kycResponse = response,
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
    
    fun clearResponse() {
        _uiState.value = _uiState.value.copy(kycResponse = null)
    }
    
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
