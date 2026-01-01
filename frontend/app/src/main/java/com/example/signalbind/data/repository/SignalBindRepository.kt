package com.example.signalbind.data.repository

import com.example.signalbind.data.api.SignalBindApiService
import com.example.signalbind.data.model.ConsentReceipt
import com.example.signalbind.data.model.KycRequest
import com.example.signalbind.data.model.KycResponse
import com.example.signalbind.data.model.VerificationRequest

class SignalBindRepository(
    private val apiService: SignalBindApiService
) {
    
    suspend fun verifyMobileNumber(
        mobileNumber: String,
        mockMode: Boolean
    ): Result<ConsentReceipt> {
        return try {
            val request = VerificationRequest(mobileNumber, mockMode)
            val response = apiService.verifyMobileNumber(request)
            
            if (response.isSuccessful) {
                response.body()?.let { receipt ->
                    Result.success(receipt)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("API Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun submitKycForm(
        kycRequest: KycRequest
    ): Result<KycResponse> {
        return try {
            val response = apiService.submitKycForm(kycRequest)
            
            if (response.isSuccessful) {
                response.body()?.let { kycResponse ->
                    Result.success(kycResponse)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("API Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
