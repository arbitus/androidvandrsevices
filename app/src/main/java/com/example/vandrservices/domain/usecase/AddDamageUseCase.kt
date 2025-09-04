package com.example.vandrservices.domain.usecase

import com.example.vandrservices.domain.model.Damage
import com.example.vandrservices.domain.repository.DamageRepository

class AddDamageUseCase(private val repository: DamageRepository) {
    suspend operator fun invoke(damage: Damage) = repository.addDamage(damage)
}