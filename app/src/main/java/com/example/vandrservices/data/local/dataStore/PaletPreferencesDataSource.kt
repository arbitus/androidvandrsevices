package com.example.vandrservices.data.local.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.vandrservices.PaletEntityProto
import com.example.vandrservices.PaletListProto
import com.example.vandrservices.data.local.model.PaletEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.example.vandrservices.data.mapper.toEntity

private val Context.paletDataStore: DataStore<PaletListProto> by dataStore(
    fileName = "palets.pb",
    serializer = PaletSerializer
)

class PaletPreferencesDataSource(private val context: Context) {

    fun getPalets(): Flow<List<PaletEntity>> =
        context.paletDataStore.data.map { proto ->
            proto.paletsList.map {
                PaletEntity(
                    it.localId, it.lotId, it.paletNumber, it.variety, it.variedad,
                    it.grower, it.packageSize, it.packDate, it.traceability, it.ageAtInspection,
                    it.boxes, it.sack, it.cases, it.label, it.pressurePSI, it.brix,
                    it.openAppearance, it.internalColor, it.externalColor, it.count, it.cat,
                    it.weight, it.mixdSize, it.pulpTemperature, it.gradeMaximum,
                    it.gradeMinimum, it.lengthMaximum, it.lengthMinimum, it.bloom,
                    it.groundColorGreen, it.groundColorTurning, it.groundColorYellow,
                    it.blushAverage, it.cblush, it.firmnessHard, it.firmnessSensitive,
                    it.firmnessSoft, it.plu, it.countMin, it.countMax, it.totalQuality,
                    it.totalCondition, it.qualityColor, it.conditionColor, it.qualityValue,
                    it.conditionValue, it.c78, it.c89, it.c910, it.c1011, it.c39, it.c4049,
                    it.c50, it.c2Dedos, it.c3Dedos, it.c4Dedos, it.c5Dedos, it.c6Dedos,
                    it.c7Dedos, it.c8Dedos, it.c9Dedos, it.c10Dedos, it.c11Dedos, it.commodity,
                    it.totalCount
                )
            }
    }


    suspend fun addPalet(palet: PaletEntity) {
        context.paletDataStore.updateData { proto ->
            proto.toBuilder()
                .addPalets(
                    PaletEntityProto.newBuilder()
                        .setLocalId(palet.localId)
                        .setLotId(palet.lotId)
                        .setPaletNumber(palet.paletNumber)
                        .setVariety(palet.variety ?: "")
                        .setVariedad(palet.variedad ?: "")
                        .setGrower(palet.grower ?: 0)
                        .setPackageSize(palet.packageSize ?: "")
                        .setPackDate(palet.packDate ?: "")
                        .setTraceability(palet.traceability ?: "")
                        .setAgeAtInspection(palet.ageAtInspection ?: "")
                        .setBoxes(palet.boxes ?: 0)
                        .setSack(palet.sack ?: "")
                        .setCases(palet.cases ?: 0)
                        .setLabel(palet.label ?: "")
                        .setPressurePSI(palet.pressurePSI ?: "")
                        .setBrix(palet.brix ?: "")
                        .setOpenAppearance(palet.openAppearance ?: "")
                        .setInternalColor(palet.internalColor ?: "")
                        .setExternalColor(palet.externalColor ?: "")
                        .setCount(palet.count ?: 0)
                        .setCat(palet.cat ?: "")
                        .setWeight(palet.weight ?: 0.0)
                        .setMixdSize(palet.mixdSize ?: 0.0)
                        .setPulpTemperature(palet.pulpTemperature ?: 0.0)
                        .setGradeMaximum(palet.gradeMaximum ?: 0.0)
                        .setGradeMinimum(palet.gradeMinimum ?: 0.0)
                        .setLengthMaximum(palet.lengthMaximum ?: 0.0)
                        .setLengthMinimum(palet.lengthMinimum ?: 0.0)
                        .setBloom(palet.bloom ?: "")
                        .setGroundColorGreen(palet.groundColorGreen ?: "")
                        .setGroundColorTurning(palet.groundColorTurning ?: "")
                        .setGroundColorYellow(palet.groundColorYellow ?: "")
                        .setBlushAverage(palet.blushAverage ?: "")
                        .setCblush(palet.cblush ?: "")
                        .setFirmnessHard(palet.firmnessHard ?: "")
                        .setFirmnessSensitive(palet.firmnessSensitive ?: "")
                        .setFirmnessSoft(palet.firmnessSoft ?: "")
                        .setPlu(palet.plu ?: "")
                        .setCountMin(palet.countMin ?: 0)
                        .setCountMax(palet.countMax ?: 0)
                        .setTotalQuality(palet.totalQuality ?: 0.0)
                        .setTotalCondition(palet.totalCondition ?: 0.0)
                        .setQualityColor(palet.qualityColor ?: "")
                        .setConditionColor(palet.conditionColor ?: "")
                        .setQualityValue(palet.qualityValue ?: "")
                        .setConditionValue(palet.conditionValue ?: "")
                        .setC78(palet.c7_8 ?: 0)
                        .setC89(palet.c8_9 ?: 0)
                        .setC910(palet.c9_10 ?: 0)
                        .setC1011(palet.c10_11 ?: 0)
                        .setC39(palet.c39 ?: 0)
                        .setC4049(palet.c40_49 ?: 0)
                        .setC50(palet.c50 ?: 0)
                        .setC2Dedos(palet.c2_dedos ?: 0)
                        .setC3Dedos(palet.c3_dedos ?: 0)
                        .setC4Dedos(palet.c4_dedos ?: 0)
                        .setC5Dedos(palet.c5_dedos ?: 0)
                        .setC6Dedos(palet.c6_dedos ?: 0)
                        .setC7Dedos(palet.c7_dedos ?: 0)
                        .setC8Dedos(palet.c8_dedos ?: 0)
                        .setC9Dedos(palet.c9_dedos ?: 0)
                        .setC10Dedos(palet.c10_dedos ?: 0)
                        .setC11Dedos(palet.c11_dedos ?: 0)
                        .setCommodity(palet.commodity ?: "")
                        .setTotalCount(palet.totalCount ?: 0)
                        .build()

                ).build()
        }
    }
    suspend fun clearPalets() {
        context.paletDataStore.updateData {
            it.toBuilder().clearPalets().build()
        }
    }

    suspend fun deletePalet(localId: String) {
        context.paletDataStore.updateData { proto ->
            val newList = proto.paletsList.filter { it.localId != localId }
            proto.toBuilder().clearPalets().addAllPalets(newList).build()
        }
    }

    fun getPaletsByLotId(localId: String): Flow<List<PaletEntity>> =
        context.paletDataStore.data.map { proto ->
            proto.paletsList
                .filter { it.lotId == localId }
                .map {
                    PaletEntity(
                        it.localId, it.lotId, it.paletNumber, it.variety, it.variedad,
                        it.grower, it.packageSize, it.packDate, it.traceability, it.ageAtInspection,
                        it.boxes, it.sack, it.cases, it.label, it.pressurePSI, it.brix,
                        it.openAppearance, it.internalColor, it.externalColor, it.count, it.cat,
                        it.weight, it.mixdSize, it.pulpTemperature, it.gradeMaximum,
                        it.gradeMinimum, it.lengthMaximum, it.lengthMinimum, it.bloom,
                        it.groundColorGreen, it.groundColorTurning, it.groundColorYellow,
                        it.blushAverage, it.cblush, it.firmnessHard, it.firmnessSensitive,
                        it.firmnessSoft, it.plu, it.countMin, it.countMax, it.totalQuality,
                        it.totalCondition, it.qualityColor, it.conditionColor, it.qualityValue,
                        it.conditionValue, it.c78, it.c89, it.c910, it.c1011, it.c39, it.c4049,
                        it.c50, it.c2Dedos, it.c3Dedos, it.c4Dedos, it.c5Dedos, it.c6Dedos,
                        it.c7Dedos, it.c8Dedos, it.c9Dedos, it.c10Dedos, it.c11Dedos, it.commodity,
                        it.totalCount
                    )
                }
        }
}