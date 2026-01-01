package com.example.signalbind.data.model

import com.google.gson.annotations.SerializedName

data class KycResponse(
    @SerializedName("matchResult")
    val matchResult: Boolean,
    
    @SerializedName("confidenceScore")
    val confidenceScore: Double?,
    
    @SerializedName("matchDetails")
    val matchDetails: MatchDetails?,
    
    @SerializedName("timestamp")
    val timestamp: String,
    
    @SerializedName("requestId")
    val requestId: String?
)

data class MatchDetails(
    @SerializedName("nameMatch")
    val nameMatch: Boolean?,
    
    @SerializedName("documentMatch")
    val documentMatch: Boolean?,
    
    @SerializedName("addressMatch")
    val addressMatch: Boolean?,
    
    @SerializedName("phoneMatch")
    val phoneMatch: Boolean?,
    
    @SerializedName("emailMatch")
    val emailMatch: Boolean?
)
