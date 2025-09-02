package com.example.vandrservices.data.SyncServices

import android.content.Context
import android.util.Log
import com.example.vandrservices.data.ApiService
import com.example.vandrservices.data.RetrofitControles
import com.example.vandrservices.data.local.dataStore.LotPreferencesDataSource
import com.example.vandrservices.data.local.dataStore.PaletPreferencesDataSource
import com.example.vandrservices.domain.model.PaletToJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class SyncPalet {
    companion object {
        fun syncPalet(context: Context, localId: String, lotId: Int? ) {
            // Lógica para sincronizar los palets
            Log.i(
                "NetworkMonitor",
                "Entra a la creacion de palet: ${localId} , ${lotId}"
            )
            val paletPreferencesDataSource = PaletPreferencesDataSource(context)
            lateinit var retrofit: Retrofit
            val apiService by lazy { retrofit.create(ApiService::class.java) }
            retrofit = RetrofitControles.getRetrofit()
            CoroutineScope(Dispatchers.IO).launch {
                val palets = paletPreferencesDataSource.getPaletsByLotId(localId).first()
                Log.i(
                    "NetworkMonitor",
                    "Palet encontrados: ${palets}"
                )
                val response = apiService.PostToken("fabian", "Zarathustra40")
                val tokenResponse = response.body()
                if (response.isSuccessful && tokenResponse != null) {
                    palets.forEach { localPalet ->
                        try {
                            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val parsedDate = inputFormat.parse(localPalet.packDate?: "")
                            val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                            outputFormat.timeZone = TimeZone.getTimeZone("UTC")
                            val packDate = outputFormat.format(parsedDate!!)
                            val palet = PaletToJson(
                                id = null,
                                lot = lotId,
                                paletNumber = localPalet.paletNumber,
                                variety = localPalet.variety,
                                variedad = localPalet.variedad,
                                grower = localPalet.grower,
                                packageSize = localPalet.packageSize,
                                packDate = packDate,
                                traceability = localPalet.traceability,
                                ageAtInspection = localPalet.ageAtInspection,
                                boxes = localPalet.boxes,
                                sack = localPalet.sack,
                                cases = localPalet.cases,
                                label = localPalet.label,
                                pressurePSI = localPalet.pressurePSI,
                                brix = localPalet.brix,
                                openAppearance = localPalet.openAppearance,
                                internalColor = localPalet.internalColor,
                                externalColor = localPalet.externalColor,
                                count = localPalet.count,
                                cat = localPalet.cat,
                                weight = localPalet.weight,
                                mixdSize = localPalet.mixdSize,
                                pulpTemperature = localPalet.pulpTemperature,
                                gradeMaximum = localPalet.gradeMaximum,
                                gradeMinimum = localPalet.gradeMinimum,
                                lengthMaximum = localPalet.lengthMaximum,
                                lengthMinimum = localPalet.lengthMinimum,
                                bloom = localPalet.bloom,
                                groundColorGreen = localPalet.groundColorGreen,
                                groundColorTurning = localPalet.groundColorTurning,
                                groundColorYellow = localPalet.groundColorYellow,
                                blushAverage = localPalet.blushAverage,
                                cblush = localPalet.cblush,
                                firmnessHard = localPalet.firmnessHard,
                                firmnessSensitive = localPalet.firmnessSensitive,
                                firmnessSoft = localPalet.firmnessSoft,
                                plu = localPalet.plu,
                                countMin = localPalet.countMin,
                                countMax = localPalet.countMax,
                                totalQuality = localPalet.totalQuality,
                                totalCondition = localPalet.totalCondition,
                                qualityColor = localPalet.qualityColor,
                                conditionColor = localPalet.conditionColor,
                                qualityValue = localPalet.qualityValue,
                                conditionValue = localPalet.conditionValue,
                                c7_8 = localPalet.c7_8,
                                c8_9 = localPalet.c8_9,
                                c9_10 = localPalet.c9_10,
                                c10_11 = localPalet.c10_11,
                                c39 = localPalet.c39,
                                c40_49 = localPalet.c40_49,
                                c50 = localPalet.c50,
                                c_2_dedos = localPalet.c2_dedos,
                                c_3_dedos = localPalet.c3_dedos,
                                c_4_dedos = localPalet.c4_dedos,
                                c_5_dedos = localPalet.c5_dedos,
                                c_6_dedos = localPalet.c6_dedos,
                                c_7_dedos = localPalet.c7_dedos,
                                c_8_dedos = localPalet.c8_dedos,
                                c_9_dedos = localPalet.c9_dedos,
                                c_10_dedos = localPalet.c10_dedos,
                                c_11_dedos = localPalet.c11_dedos,
                                commodity = localPalet.commodity,
                                totalCount = localPalet.totalCount
                            )
                            val response = apiService.createPalet(
                                "Bearer ${tokenResponse.access}", palet
                            )
                            if (response.isSuccessful && response.body()?.id != null) {
                                // Elimina el palet local si fue exitoso
                                paletPreferencesDataSource.deletePalet(localPalet.localId)
                                Log.i(
                                    "NetworkMonitor",
                                    "Palet sincronizado y eliminado localmente: ${localPalet.localId}"
                                )
                            }
                        } catch (e: Exception) {
                            Log.e(
                                "NetworkMonitor",
                                "Error sincronizando palet ${localPalet.localId}: ${e.message}"
                            )
                        }
                    }
                }
            }
        }
    }
}