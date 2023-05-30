package com.example.rickandmortyapp.network

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiServer {

    @GET("api/character/{id}")
    suspend fun getSelectedCharacter(@Path("id") id: Int): ResultCharacter

    @GET("api/character")
    suspend fun getCharacter(): CharacterRequest

    @GET("api/episode/{id}")
    suspend fun getEpisode(@Path("id") id: Int): Episode
}