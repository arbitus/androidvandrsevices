package com.example.vandrservices.domain.usecase

import com.example.vandrservices.domain.model.Palet
import com.example.vandrservices.domain.repository.PaletRepository

class AddPaletUseCase(private val repository: PaletRepository) {
    suspend operator fun invoke(palet: Palet) = repository.addPalet(palet)
}