package com.example.vandrservices.data.mapper

import com.example.vandrservices.data.local.model.DamageEntity
import com.example.vandrservices.domain.model.Damage

fun DamageEntity.toDomain() = Damage(
    localId, palet, name, type, value
)

fun Damage.toEntity() = DamageEntity(
    localId, palet, name, type, value
)