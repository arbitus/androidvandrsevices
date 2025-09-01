package com.example.vandrservices.domain.usecase

import com.example.vandrservices.domain.repository.LotRepository

class ClearLotsUseCase(private val repository: LotRepository) {
    suspend operator fun invoke() = repository.clearLots()
}