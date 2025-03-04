package com.gokmenmutlu.example2weatherapp.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {

    companion object {
        const val BASE_URL = "https://api.openweathermap.org"
    }

    private lateinit var retrofit: Retrofit

    private val clientOkHttp = OkHttpClient.Builder()
        .connectTimeout(30,TimeUnit.SECONDS)
        .readTimeout(30,TimeUnit.SECONDS)
        .writeTimeout(30,TimeUnit.SECONDS)
        .build()

    fun getClient(): Retrofit {

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(clientOkHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit
    }

}