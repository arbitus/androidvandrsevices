package com.example.vandrservices

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

object NetworkMonitor {

    private var connectivityManager: ConnectivityManager? = null
    private val listeners = mutableListOf<(Boolean) -> Unit>()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            notifyListeners(true)
        }

        override fun onLost(network: Network) {
            notifyListeners(false)
        }
    }

    fun init(context: Context) {
        if (connectivityManager != null) return // evitar doble init
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager?.registerNetworkCallback(request, networkCallback)
    }

    fun addListener(listener: (Boolean) -> Unit) {
        listeners.add(listener)
    }

    fun removeListener(listener: (Boolean) -> Unit) {
        listeners.remove(listener)
    }

    private fun notifyListeners(available: Boolean) {
        listeners.forEach { it(available) }
    }
}
