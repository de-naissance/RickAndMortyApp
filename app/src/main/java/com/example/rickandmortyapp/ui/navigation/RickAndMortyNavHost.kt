package com.example.rickandmortyapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rickandmortyapp.ui.AppViewModelProvider
import com.example.rickandmortyapp.ui.screens.HomeDestination
import com.example.rickandmortyapp.ui.screens.HomeScreen
import com.example.rickandmortyapp.ui.screens.HomeViewModel

@Composable
fun RickAndMortyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val rickAndMortyViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)

    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                rickAndMortyViewModel.appUiState
            )
        }

    }
}