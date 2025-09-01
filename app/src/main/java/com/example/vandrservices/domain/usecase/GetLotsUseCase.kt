package com.example.vandrservices.domain.usecase

import com.example.vandrservices.domain.repository.LotRepository

class GetLotsUseCase(private val repository: LotRepository) {
    operator fun invoke() = repository.getLots()
}