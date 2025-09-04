package com.example.vandrservices.domain.usecase

import com.example.vandrservices.domain.model.User
import com.example.vandrservices.domain.repository.UserRepository

class UpdateUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(user: User) = repository.updateUserToken(user)
}