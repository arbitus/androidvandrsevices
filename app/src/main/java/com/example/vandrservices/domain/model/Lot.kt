package com.example.vandrservices.domain.model

import com.google.gson.annotations.SerializedName

data class Lot(
    val localId: String,
    val company: String,
    val lotNumber: String,
    val arrivalPort: String,
    val insPlace: String,
    val insDate: String,
    val exporter: String,
    val invoice: String,
    val arvWeek: String,
    val origin: String,
    val auditor: String,
    val cases: String,
    val grower: String,
    val label: String,
    val variety: String
)

data class LotToJson(
    @SerializedName("id") val id: Int?,
    @SerializedName("arrivalport") val arrivalPort: String?,
    @SerializedName("arrivalweek") val arrivalWeek: Int?,
    @SerializedName("auditor") val auditor: String?,
    @SerializedName("cases") val cases: Int?,
    @SerializedName("company") val company: Int, // ID de la compañía
    @SerializedName("exporter") val exporter: String?,
    @SerializedName("grower") val grower: String?,
    @SerializedName("insdate") val insDate: String?, // Formato ISO 8601
    @SerializedName("insplace") val insPlace: String?,
    @SerializedName("invoice") val invoice: String?,
    @SerializedName("label") val label: String?,
    @SerializedName("lotnumber") val lotNumber: String?,
    @SerializedName("origin") val origin: String?,
    @SerializedName("variedad") val variedad: String?
)