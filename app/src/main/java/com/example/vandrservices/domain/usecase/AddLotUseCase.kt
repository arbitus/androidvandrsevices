package com.example.vandrservices.domain.usecase

import com.example.vandrservices.domain.model.Lot
import com.example.vandrservices.domain.repository.LotRepository

class AddLotUseCase(private val repository: LotRepository) {
    suspend operator fun invoke(lot: Lot) = repository.addLot(lot)
}