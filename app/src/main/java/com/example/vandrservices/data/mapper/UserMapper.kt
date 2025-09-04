package com.example.vandrservices.data.mapper

import com.example.vandrservices.data.local.model.UserEntity
import com.example.vandrservices.domain.model.User

fun UserEntity.toDomain() = User(
    userId, name, password, token
)

fun User.toEntity() = UserEntity(
    userId, name, password, token
)