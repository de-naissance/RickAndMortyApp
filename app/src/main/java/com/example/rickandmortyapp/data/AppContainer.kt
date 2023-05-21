package com.example.rickandmortyapp.data

import com.example.rickandmortyapp.network.ApiServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val appRepository: AppRepository
}

class DefaultAppContainer: AppContainer {
    private val BASE_URL = "https://rickandmortyapi.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitService: ApiServer by lazy {
        retrofit.create(ApiServer::class.java)
    }

    override val appRepository: AppRepository by lazy {
        NetworkRepository(retrofitService)
    }
}