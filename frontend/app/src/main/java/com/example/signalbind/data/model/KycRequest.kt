package com.example.signalbind.data.model

import com.google.gson.annotations.SerializedName

data class KycRequest(
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    
    @SerializedName("idDocument")
    val idDocument: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("givenName")
    val givenName: String,
    
    @SerializedName("familyName")
    val familyName: String,
    
    @SerializedName("middleNames")
    val middleNames: String?,
    
    @SerializedName("familyNameAtBirth")
    val familyNameAtBirth: String?,
    
    @SerializedName("nameKanaHankaku")
    val nameKanaHankaku: String?,
    
    @SerializedName("nameKanaZenkaku")
    val nameKanaZenkaku: String?,
    
    @SerializedName("birthdate")
    val birthdate: String, // Format: YYYY-MM-DD
    
    @SerializedName("gender")
    val gender: String, // "Male", "Female", "Other"
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("address")
    val address: String?,
    
    @SerializedName("streetName")
    val streetName: String?,
    
    @SerializedName("streetNumber")
    val streetNumber: String?,
    
    @SerializedName("houseNumberExtension")
    val houseNumberExtension: String?,
    
    @SerializedName("postalCode")
    val postalCode: String?,
    
    @SerializedName("region")
    val region: String?,
    
    @SerializedName("locality")
    val locality: String?,
    
    @SerializedName("country")
    val country: String?
)
