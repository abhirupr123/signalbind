package com.example.signalbind.data.model

import com.google.gson.annotations.SerializedName

data class VerificationRequest(
    @SerializedName("mobileNumber")
    val mobileNumber: String,
    
    @SerializedName("mockMode")
    val mockMode: Boolean
)
