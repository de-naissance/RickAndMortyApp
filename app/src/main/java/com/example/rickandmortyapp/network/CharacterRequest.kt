package com.example.rickandmortyapp.network

data class CharacterRequest(
    val info: Info,
    val results: List<ResultCharacter>
)