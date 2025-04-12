package com.example.freemanstats.api

import android.app.Application
import com.example.freemanstats.api.RetrofitClient.okHttpClient
import com.example.freemanstats.api.RetrofitClient.retrofit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://api.clashofclans.com/v1/"  // Базовый URL API COC

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY  // Логирование запросов
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val clashOfClansApi: ClashOfClansApi = retrofit.create(ClashOfClansApi::class.java)
}

