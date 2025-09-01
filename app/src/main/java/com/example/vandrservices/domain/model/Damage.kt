package com.example.vandrservices.domain.model

import com.google.gson.annotations.SerializedName

data class Damage(
    val localId: String,
    val palet: Int,
    val name: String,
    val type: String,
    val value: Double
)

data class DamageToJson(
    @SerializedName("palet")val palet: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("tipo") val type: String?,
    @SerializedName("value") val value: Double?
)