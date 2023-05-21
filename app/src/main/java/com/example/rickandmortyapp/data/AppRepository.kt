package com.example.rickandmortyapp.data

import com.example.rickandmortyapp.network.ApiServer
import com.example.rickandmortyapp.network.CharacterRequest

interface AppRepository {
    suspend fun getCharacter(): CharacterRequest
}

class NetworkRepository(
    private val appApiRepository: ApiServer
): AppRepository {
    override suspend fun getCharacter(): CharacterRequest {
        return appApiRepository.getCharacter()
    }

}