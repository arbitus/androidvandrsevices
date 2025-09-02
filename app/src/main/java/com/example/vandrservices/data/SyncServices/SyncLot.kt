package com.example.vandrservices.data.SyncServices

import android.content.Context
import android.util.Log
import com.example.vandrservices.data.ApiService
import com.example.vandrservices.data.RetrofitControles
import com.example.vandrservices.data.SyncServices.SyncPalet.Companion.syncPalet
import com.example.vandrservices.data.local.dataStore.LotPreferencesDataSource;
import com.example.vandrservices.domain.model.LotToJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class SyncLot {
    companion object {
        fun syncLots(context: Context) {
            val lotPreferencesDataSource = LotPreferencesDataSource(context)
            lateinit var retrofit: Retrofit
            val apiService by lazy { retrofit.create(ApiService::class.java) }
            retrofit = RetrofitControles.getRetrofit()
            CoroutineScope(Dispatchers.IO).launch {
                val lots = lotPreferencesDataSource.getLots().first()
                val response = apiService.PostToken("fabian", "Zarathustra40")
                val tokenResponse = response.body()
                if (response.isSuccessful && tokenResponse != null) {

                    lots.forEach { localLot ->
                        try {
                            // Convierte LotEntity a LotToJson si es necesario
                            var apiDate = localLot.insDate
                            val inputFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                            val parsedDate = inputFormat.parse(apiDate)
                            val outputFormat =
                                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                            outputFormat.timeZone = TimeZone.getTimeZone("UTC")
                            apiDate = outputFormat.format(parsedDate!!)
                            val lotToJson = LotToJson(
                                id = null,
                                arrivalPort = localLot.arrivalPort,
                                arrivalWeek = localLot.arvWeek.toIntOrNull(),
                                auditor = localLot.auditor,
                                cases = localLot.cases.toIntOrNull(),
                                company = localLot.company.toInt(),
                                exporter = localLot.exporter,
                                grower = localLot.grower,
                                insDate = apiDate,
                                insPlace = localLot.insPlace,
                                invoice = localLot.invoice,
                                label = localLot.label,
                                lotNumber = localLot.lotNumber,
                                origin = localLot.origin,
                                variedad = localLot.variety
                            )

                            val response =
                                apiService.createLot("Bearer ${tokenResponse.access}", lotToJson)
                            if (response.isSuccessful && response.body()?.id != null) {
                                // Elimina el lote local si fue exitoso
                                syncPalet(context, localLot.localId, response.body()!!.id)
                                lotPreferencesDataSource.deleteLot(localLot.localId)
                                Log.i(
                                    "NetworkMonitor",
                                    "Lote sincronizado y eliminado localmente: ${localLot.localId}"
                                )
                            }
                        } catch (e: Exception) {
                            Log.e(
                                "NetworkMonitor",
                                "Error sincronizando lote ${localLot.localId}: ${e.message}"
                            )
                        }
                    }
                }
            }
        }
    }
}