package com.example.rickandmortyapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.rickandmortyapp.ui.AppViewModelProvider
import com.example.rickandmortyapp.ui.screens.homeScreen.HomeDestination
import com.example.rickandmortyapp.ui.screens.homeScreen.HomeScreen
import com.example.rickandmortyapp.ui.screens.homeScreen.HomeViewModel
import com.example.rickandmortyapp.ui.screens.informationScreen.InformationScreen
import com.example.rickandmortyapp.ui.screens.informationScreen.InformationViewModel
import com.example.rickandmortyapp.ui.screens.informationScreen.SelectedCharacter
import com.example.rickandmortyapp.ui.screens.filterScreen.SearchDestination
import com.example.rickandmortyapp.ui.screens.filterScreen.FilterScreen

@Composable
fun RickAndMortyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            val homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)

            HomeScreen(
                homeViewModel.appUiState,
                navigateToInformation = {
                    navController.navigate("${SelectedCharacter.route}/$it")
                },
                navigateToSearch = {
                    navController.navigate(SearchDestination.route)
                },
                selectLayout = homeViewModel::selectLayout,
                layoutUiState = homeViewModel.layoutUiState.collectAsState().value,
                selectDarkMode = homeViewModel::selectDarkMode,
                darkMode = homeViewModel.darkModeState.collectAsState().value
            )
        }

        composable(
            route = SelectedCharacter.routeWithArgs,
            arguments = listOf(navArgument(SelectedCharacter.itemIdArg) {
                type = NavType.IntType
                nullable = false
            })
        ) {
            val informationViewModel: InformationViewModel = viewModel(factory = AppViewModelProvider.Factory)

            InformationScreen(
                navigationBack = { navController.navigateUp() },

                )
        }
        composable(route = SearchDestination.route) {
            val homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)

            FilterScreen(
                navigationBack = { navController.navigateUp() },
                viewModel = homeViewModel,
                onSendButtonClicked = {
                    navController.navigate(HomeDestination.route)
                }
            )
        }
    }
}
