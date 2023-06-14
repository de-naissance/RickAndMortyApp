package com.example.rickandmortyapp.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServer {

    @GET("api/character/{id}")
    suspend fun getSelectedCharacter(@Path("id") id: Int): ResultCharacter

    @GET("api/character")
    suspend fun getCharacter(
        @Query("page") page: Int? = null,
        @Query("name") name: String,
        @Query("status") status: String,
        @Query("species") species: String,
        @Query("type") type: String,
        @Query("gender") gender: String,
    ): CharacterRequest

    @GET("api/episode/{id}")
    suspend fun getEpisode(@Path("id") id: Int): Episode
}