package com.example.vandrservices.data

import com.example.vandrservices.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitControles {
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://localhost:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    companion object {
        fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}