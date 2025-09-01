package com.example.vandrservices

import android.app.Application
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.example.vandrservices.data.local.dataStore.LotPreferencesDataSource
import com.example.vandrservices.ui.form.lot.LotCreationViewModel
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltAndroidApp
class VandrservicesApp: Application(){
    override fun onCreate() {
        super.onCreate()
        // Iniciamos el monitor global al arrancar la app
        NetworkMonitor.init(this)
        NetworkMonitor.addListener { isAvailable ->
            if (isAvailable) {
                Log.i("NetworkMonitor", "Internet volvió 🚀, intentando login...")
                val lotPreferencesDataSource = LotPreferencesDataSource(this)

                CoroutineScope(Dispatchers.IO).launch {
                    lotPreferencesDataSource.clearLots()
                }
            } else {
                Log.w("NetworkMonitor", "Sin internet ❌")
            }
        }
    }
}