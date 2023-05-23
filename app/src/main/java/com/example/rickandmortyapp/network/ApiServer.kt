package com.example.rickandmortyapp.network

import retrofit2.http.GET

interface ApiServer {

    @GET("api/character")
    suspend fun getListCharacter(): List<ResultCharacter>

    @GET("api/character")
    suspend fun getCharacter(): CharacterRequest
}