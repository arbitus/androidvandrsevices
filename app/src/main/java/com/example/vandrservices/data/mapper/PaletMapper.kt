package com.example.vandrservices.data.mapper

import com.example.vandrservices.data.local.model.PaletEntity
import com.example.vandrservices.domain.model.Palet


fun PaletEntity.toDomain() = Palet(
    localId, lotId, paletNumber, variedad, variety, grower, packageSize, packDate,
    traceability, ageAtInspection, boxes, sack, cases, label, pressurePSI, brix,
    openAppearance, internalColor, externalColor, count, cat, weight, mixdSize,
    pulpTemperature, gradeMaximum, gradeMinimum, lengthMaximum, lengthMinimum,
    bloom, groundColorGreen, groundColorTurning, groundColorYellow, blushAverage,
    cblush, firmnessHard, firmnessSensitive, firmnessSoft, plu, countMin, countMax,
    totalQuality, totalCondition, qualityColor, conditionColor, qualityValue, conditionValue,
    c7_8, c8_9, c9_10, c10_11, c39, c40_49, c50, c2_dedos, c3_dedos, c4_dedos, c5_dedos,
    c6_dedos, c7_dedos, c8_dedos, c9_dedos, c10_dedos, c11_dedos, commodity, totalCount
)

fun Palet.toEntity() = PaletEntity(
    localId, lotId, paletNumber, variedad, variety, grower, packageSize, packDate,
    traceability, ageAtInspection, boxes, sack, cases, label, pressurePSI, brix,
    openAppearance, internalColor, externalColor, count, cat, weight, mixdSize,
    pulpTemperature, gradeMaximum, gradeMinimum, lengthMaximum, lengthMinimum,
    bloom, groundColorGreen, groundColorTurning, groundColorYellow, blushAverage,
    cblush, firmnessHard, firmnessSensitive, firmnessSoft, plu, countMin, countMax,
    totalQuality, totalCondition, qualityColor, conditionColor, qualityValue, conditionValue,
    c7_8, c8_9, c9_10, c10_11, c39, c40_49, c50, c2_dedos, c3_dedos, c4_dedos, c5_dedos,
    c6_dedos, c7_dedos, c8_dedos, c9_dedos, c10_dedos, c11_dedos, commodity, totalCount
)