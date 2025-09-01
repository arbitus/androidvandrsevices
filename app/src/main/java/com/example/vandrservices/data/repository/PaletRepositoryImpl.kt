package com.example.vandrservices.data.repository

import com.example.vandrservices.data.local.dataStore.PaletPreferencesDataSource
import com.example.vandrservices.data.mapper.toDomain
import com.example.vandrservices.data.mapper.toEntity
import com.example.vandrservices.domain.model.Lot
import com.example.vandrservices.domain.model.Palet
import com.example.vandrservices.domain.repository.PaletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PaletRepositoryImpl (
    private val dataSource: PaletPreferencesDataSource
) : PaletRepository {

    override suspend fun addPalet(palet: Palet) {
        dataSource.addPalet(palet.toEntity())
    }
    override fun getPalets(): Flow<List<Palet>> =
        dataSource.getPalets().map { list -> list.map { it.toDomain() } }

    override suspend fun clearPalets() {
        dataSource.clearPalets()
    }
}