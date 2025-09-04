package com.example.vandrservices.data.repository

import com.example.vandrservices.data.ApiService
import com.example.vandrservices.data.local.dataStore.UserPreferencesDataSource
import com.example.vandrservices.data.mapper.toDomain
import com.example.vandrservices.data.mapper.toEntity
import com.example.vandrservices.domain.model.User
import com.example.vandrservices.domain.repository.UserRepository
import com.example.vandrservices.data.RetrofitControles
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val dataSource: UserPreferencesDataSource
) : UserRepository {

    private var token: String? = null
    lateinit var retrofit: Retrofit
    val apiService by lazy { retrofit.create(ApiService::class.java) }

    override suspend fun addUser(user: User) {
        dataSource.addUser(user.toEntity())
    }

    override fun getUsers(): Flow<List<User>> =
        dataSource.getUsers().map { list -> list.map { it.toDomain() } }

    override suspend fun login(username: String, password: String): String {
        retrofit = RetrofitControles.getRetrofit()
        try{
            val response = apiService.PostToken(username, password)
            if (response.isSuccessful) {
                token =
                    response.body()?.access?: ""
                return token as String
            } else {
                return ""
            }
        } catch (e: Exception) {
            return ""
        }
    }
    override fun getToken(): String? = token
    override suspend fun updateUserToken(user: User) {
        dataSource.updateUserToken(user.userId, user.token)
    }
}