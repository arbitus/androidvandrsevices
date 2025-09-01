package com.example.vandrservices.domain.repository

import com.example.vandrservices.domain.model.Lot
import kotlinx.coroutines.flow.Flow

interface LotRepository {
    suspend fun addLot(lot: Lot)
    fun getLots(): Flow<List<Lot>>
    suspend fun clearLots()
}