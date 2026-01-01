package com.example.signalbind.data.api

import com.example.signalbind.data.model.ConsentReceipt
import com.example.signalbind.data.model.KycRequest
import com.example.signalbind.data.model.KycResponse
import com.example.signalbind.data.model.VerificationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignalBindApiService {
    
    @POST("verify")
    suspend fun verifyMobileNumber(
        @Body request: VerificationRequest
    ): Response<ConsentReceipt>
    
    @POST("kyc-match")
    suspend fun submitKycForm(
        @Body request: KycRequest
    ): Response<KycResponse>
}
