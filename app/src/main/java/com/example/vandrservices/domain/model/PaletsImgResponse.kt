package com.example.vandrservices.domain.model

data class PaletsImgResponse(
    val id: Int,
    val lot_number: String,
    val palet_number: String,
    val tipo: String?,
    val imagen: String
)