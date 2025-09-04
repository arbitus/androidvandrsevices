package com.example.vandrservices.data.repository

import com.example.vandrservices.data.ApiService
import com.example.vandrservices.data.RetrofitControles
import com.example.vandrservices.data.local.dataStore.DamagePreferencesDataSource
import com.example.vandrservices.data.local.dataStore.UserPreferencesDataSource
import com.example.vandrservices.data.mapper.toDomain
import com.example.vandrservices.domain.model.User
import com.example.vandrservices.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Retrofit
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val dataSource: UserPreferencesDataSource
) : LoginRepository {
    override suspend fun login(username: String, password: String): String {
        lateinit var retrofit: Retrofit
        val apiService by lazy { retrofit.create(ApiService::class.java) }
        retrofit = RetrofitControles.getRetrofit()

        val response = apiService.PostToken(username, password)
        if (response.isSuccessful) {
            return response.body()?.access ?: ""
        }
        return ""
    }

    fun getUser(): Flow<List<User>> =
        dataSource.getUsers().map { list -> list.map { it.toDomain() }
    }
}