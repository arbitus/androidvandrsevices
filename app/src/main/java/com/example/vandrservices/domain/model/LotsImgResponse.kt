package com.example.vandrservices.domain.model

data class LotsImgResponse(
    val id: Int,
    val lot_number: String,
    val tipo: String?,
    val imagen: String
)