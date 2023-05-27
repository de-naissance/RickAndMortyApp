package com.example.rickandmortyapp.network

import retrofit2.http.GET

interface ApiServer {

    @GET("api/character/{id}")
    suspend fun getSelectedCharacter(id: Int): ResultCharacter

    @GET("api/character")
    suspend fun getCharacter(): CharacterRequest
}