package com.example.vandrservices.data.repository

import com.example.vandrservices.data.local.dataStore.LotPreferencesDataSource
import com.example.vandrservices.data.mapper.toEntity
import com.example.vandrservices.data.mapper.toDomain
import com.example.vandrservices.domain.model.Lot
import com.example.vandrservices.domain.repository.LotRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LotRepositoryImpl(
    private val dataSource: LotPreferencesDataSource
) : LotRepository {

    override suspend fun addLot(lot: Lot) {
        dataSource.addLot(lot.toEntity())
    }

    override fun getLots(): Flow<List<Lot>> =
        dataSource.getLots().map { list -> list.map { it.toDomain() } }

    override suspend fun clearLots() {
        dataSource.clearLots()
    }
    suspend fun deleteLot(localId: String) {
        dataSource.deleteLot(localId)
    }

}