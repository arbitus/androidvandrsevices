package com.example.vandrservices.domain.usecase

import com.example.vandrservices.domain.repository.PaletRepository

class ClearPaletsUseCase(private val repository: PaletRepository) {
    suspend operator fun invoke() = repository.clearPalets()
}