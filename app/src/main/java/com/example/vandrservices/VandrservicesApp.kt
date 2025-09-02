package com.example.vandrservices

import android.app.Application
import android.util.Log
import com.example.vandrservices.data.ApiService
import com.example.vandrservices.data.RetrofitControles
import com.example.vandrservices.data.SyncServices.SyncLot
import dagger.hilt.android.HiltAndroidApp
import retrofit2.Retrofit

@HiltAndroidApp
class VandrservicesApp: Application(){

    override fun onCreate() {
        super.onCreate()
        // Iniciamos el monitor global al arrancar la app
        NetworkMonitor.init(this)
        NetworkMonitor.addListener { isAvailable ->
            if (isAvailable) {
                Log.i("NetworkMonitor", "Internet volvió 🚀, intentando login...")
                SyncLot.syncLots(this)
            } else {
                Log.w("NetworkMonitor", "Sin internet ❌")
            }
        }
    }
}