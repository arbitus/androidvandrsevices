package com.example.vandrservices.data.mapper

import com.example.vandrservices.data.local.model.LotEntity
import com.example.vandrservices.domain.model.Lot

fun LotEntity.toDomain() = Lot(
    localId, company, lotNumber, arrivalPort, insPlace, insDate, exporter, invoice, arvWeek,
    origin, auditor, cases, grower, label, variety
)

fun Lot.toEntity() = LotEntity(
    localId, company ,lotNumber, arrivalPort, insPlace, insDate, exporter, invoice, arvWeek,
    origin, auditor, cases, grower, label, variety
)