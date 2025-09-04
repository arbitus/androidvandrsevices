package com.example.vandrservices

import android.app.Application
import android.util.Log
import com.example.vandrservices.data.ApiService
import com.example.vandrservices.data.RetrofitControles
import com.example.vandrservices.data.SyncServices.SyncLot
import com.example.vandrservices.data.local.dataStore.DamagePreferencesDataSource
import com.example.vandrservices.data.local.dataStore.LotPreferencesDataSource
import com.example.vandrservices.data.local.dataStore.PaletPreferencesDataSource
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import kotlin.coroutines.coroutineContext

@HiltAndroidApp
class VandrservicesApp: Application(){

    override fun onCreate() {
        super.onCreate()
        // Iniciamos el monitor global al arrancar la app
        NetworkMonitor.init(this)
        NetworkMonitor.addListener { isAvailable ->
            if (isAvailable) {
                Log.i("NetworkMonitor", "Internet volvió 🚀, intentando login...")
                //val lotPreferencesDataSource = LotPreferencesDataSource(this)
                //val paletsPreferencesDataSource = PaletPreferencesDataSource(this)
                //val damagePreferencesDataSource = DamagePreferencesDataSource(this)
                //CoroutineScope(Dispatchers.IO).launch {
                //    lotPreferencesDataSource.clearLots()
                //    paletsPreferencesDataSource.clearPalets()
                //    damagePreferencesDataSource.clearDamages()
                //}
                SyncLot.syncLots(this)
            } else {
                Log.w("NetworkMonitor", "Sin internet ❌")
            }
        }
    }
}