package com.example.rickandmortyapp.data

import android.util.Log
import com.example.rickandmortyapp.network.ApiServer
import com.example.rickandmortyapp.network.CharacterRequest
import com.example.rickandmortyapp.network.Episode
import com.example.rickandmortyapp.network.ResultCharacter

interface AppRepository {
    suspend fun getCharacter(): CharacterRequest

    suspend fun getSelectedCharacter(id: Int): ResultCharacter

    suspend fun getEpisode(id: Int): Episode
}

class NetworkRepository(
    private val appApiRepository: ApiServer
): AppRepository {
    override suspend fun getCharacter(): CharacterRequest {
        return appApiRepository.getCharacter()
    }

    override suspend fun getSelectedCharacter(id: Int): ResultCharacter {
        return appApiRepository.getSelectedCharacter(id)
    }

    override suspend fun getEpisode(id: Int): Episode {
        return appApiRepository.getEpisode(id)
    }
}