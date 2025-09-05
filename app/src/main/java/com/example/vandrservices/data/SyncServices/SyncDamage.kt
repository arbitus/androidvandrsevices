package com.example.vandrservices.data.SyncServices

import android.content.Context
import android.util.Log
import com.example.vandrservices.data.ApiService
import com.example.vandrservices.data.RetrofitControles
import com.example.vandrservices.data.local.dataStore.DamagePreferencesDataSource
import com.example.vandrservices.domain.model.DamageToJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class SyncDamage {
    companion object {
        @Volatile private var isSyncingDamage = false
        fun syncDamages(context: Context, localId: String, paletId: Int) {
            if (isSyncingDamage) {
                Log.w("SyncLot", "Ya se está sincronizando, se ignora este llamado.")
                return
            }
            isSyncingDamage = true
            val damagePreferencesDataSource = DamagePreferencesDataSource(context)
            Log.i(
                "NetworkMonitor",
                "Entra a la creacion de damages: ${paletId}"
            )
            lateinit var retrofit: Retrofit
            val apiService by lazy { retrofit.create(ApiService::class.java) }
            retrofit = RetrofitControles.getRetrofit()
            CoroutineScope(Dispatchers.IO).launch {
                val damages = damagePreferencesDataSource.getDamagesByPaletId(localId.toIntOrNull()?:0).first()
                Log.i(
                    "NetworkMonitor",
                    "Damage encontrados: ${damages}"
                )
                val response = apiService.PostToken("fabian", "Zarathustra40")
                val tokenResponse = response.body()
                if (response.isSuccessful && tokenResponse != null) {
                    damages.forEach { localDamage ->
                        try {
                            val damage = DamageToJson(
                                palet = paletId,
                                name = localDamage.name,
                                type = localDamage.type,
                                value = localDamage.value
                            )
                            val response = apiService.createDamage(
                                "Bearer ${tokenResponse.access}", damage
                            )
                            if (response.isSuccessful && response.body()?.palet != null) {
                                damagePreferencesDataSource.deleteDamage(localDamage.localId)
                                Log.i(
                                    "NetworkMonitor",
                                    "Damage sincronizado y eliminado localmente: ${localDamage.localId}"
                                )
                            }
                        }catch (e: Exception) {
                            Log.e(
                                "NetworkMonitor",
                                "Error sincronizando damage ${localDamage.localId}: ${e.message}"
                            )
                        }
                    }
                }
                isSyncingDamage = false
            }
        }
    }
}