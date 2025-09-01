package com.example.vandrservices.domain.repository

import com.example.vandrservices.domain.model.Palet
import kotlinx.coroutines.flow.Flow

interface PaletRepository {
    suspend fun addPalet(palet: Palet)
    fun getPalets(): Flow<List<Palet>>
    suspend fun clearPalets()
}