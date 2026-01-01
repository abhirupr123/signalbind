package com.example.signalbind.data.model

import com.google.gson.annotations.SerializedName

data class ConsentReceipt(
    @SerializedName("receiptId")
    val receiptId: String,
    
    @SerializedName("verificationStatus")
    val verificationStatus: Boolean,
    
    @SerializedName("recycledSince")
    val recycledSince: String?,
    
    @SerializedName("reasonCode")
    val reasonCode: String?,
    
    @SerializedName("timestamp")
    val timestamp: String
)
