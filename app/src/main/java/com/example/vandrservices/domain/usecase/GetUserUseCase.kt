package com.example.vandrservices.domain.usecase

import com.example.vandrservices.domain.repository.UserRepository

class GetUserUseCase(private val repository: UserRepository) {
    operator fun invoke() = repository.getUsers()
}