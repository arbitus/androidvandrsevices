package com.example.vandrservices.domain.usecase

import com.example.vandrservices.domain.repository.DamageRepository

class GetDamagesUseCase(private val repository: DamageRepository) {
    operator fun invoke() = repository.getDamages()
}