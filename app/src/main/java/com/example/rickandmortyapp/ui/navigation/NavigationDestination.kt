package com.example.rickandmortyapp.ui.navigation

/**
 * Interface for describing navigation destinations for the app
 */
interface NavigationDestination {
    /**
     * A unique name for defining the path to a composite element
     */
    val route: String

    /**
     * The string identifier of the resource containing the title that will be displayed on the screen.
     */
    val titleRes: Int
}