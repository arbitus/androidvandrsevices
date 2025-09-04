package com.example.vandrservices.domain.repository

import com.example.vandrservices.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun addUser(user: User)
    fun getUsers(): Flow<List<User>>
    suspend fun login(username: String, password: String): String
    fun getToken(): String?
    suspend fun updateUserToken(user: User)
}