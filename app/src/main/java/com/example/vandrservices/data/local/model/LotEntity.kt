package com.example.vandrservices.data.local.model

data class LotEntity(
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