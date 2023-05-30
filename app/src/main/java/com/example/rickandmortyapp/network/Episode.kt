package com.example.rickandmortyapp.network

data class Episode(
    /** The id of the episode. */
    val id: Int,
    /** The air date of the episode. */
    val air_date: String,
    /** List of characters who have been seen in the episode. */
    val characters: List<String>,
    /** Time at which the episode was created in the database. */
    val created: String,
    /** The code of the episode. */
    val episode: String,
    /** The name of the episode. */
    val name: String,
    /** Link to the episode's own endpoint. */
    val url: String
)