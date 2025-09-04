package com.example.vandrservices.domain.usecase

import com.example.vandrservices.domain.repository.DamageRepository

class ClearDamagesUseCase(private val repository: DamageRepository) {
    suspend operator fun invoke() = repository.clearDamages()
}