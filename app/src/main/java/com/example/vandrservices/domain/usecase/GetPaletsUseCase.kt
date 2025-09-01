package com.example.vandrservices.domain.usecase

import com.example.vandrservices.domain.repository.PaletRepository

class GetPaletsUseCase(private val repository: PaletRepository) {
    operator fun invoke() = repository.getPalets()
}