package com.example.vandrservices.data.local.model

data class UserEntity(
    val userId: String,
    val name: String,
    val password: String,
    val token: String
)