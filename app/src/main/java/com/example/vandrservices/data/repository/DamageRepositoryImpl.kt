package com.example.vandrservices.data.repository

import com.example.vandrservices.data.local.dataStore.DamagePreferencesDataSource
import com.example.vandrservices.data.mapper.toDomain
import com.example.vandrservices.data.mapper.toEntity
import com.example.vandrservices.domain.model.Damage
import com.example.vandrservices.domain.repository.DamageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DamageRepositoryImpl (
    private val dataSource: DamagePreferencesDataSource
): DamageRepository {

    override suspend fun addDamage(damage: Damage) {
        dataSource.addDamage(damage.toEntity())
    }
    override fun getDamages(): Flow<List<Damage>> =
        dataSource.getDamages().map { list -> list.map { it.toDomain() } }

    override suspend fun clearDamages() {
        dataSource.clearDamages()
    }
}