package com.example.vandrservices.domain.model

data class User(
    val userId: String,
    val name: String,
    val password: String,
    val token: String
)