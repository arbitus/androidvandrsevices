package com.example.vandrservices.domain.repository

import com.example.vandrservices.domain.model.Damage
import kotlinx.coroutines.flow.Flow

interface DamageRepository {
    suspend fun addDamage(damage: Damage)
    fun getDamages(): Flow<List<Damage>>
    suspend fun clearDamages()
}