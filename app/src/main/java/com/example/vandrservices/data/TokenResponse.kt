package com.example.vandrservices.data

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("access") val access:String
)