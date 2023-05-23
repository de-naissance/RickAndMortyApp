package com.example.rickandmortyapp.network

data class CharacterRequest(
    val info: Info,
    val resultCharacters: List<ResultCharacter>
)