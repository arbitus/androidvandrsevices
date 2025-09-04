package com.example.vandrservices.domain.repository

interface LoginRepository {
    suspend fun login(username: String, password: String): String
}