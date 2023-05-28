package com.example.rickandmortyapp.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.rickandmortyapp.ui.AppViewModelProvider
import com.example.rickandmortyapp.ui.HomeDestination
import com.example.rickandmortyapp.ui.HomeScreen
import com.example.rickandmortyapp.ui.HomeViewModel
import com.example.rickandmortyapp.ui.screens.informationScreen.InformationScreen
import com.example.rickandmortyapp.ui.screens.informationScreen.InformationViewModel
import com.example.rickandmortyapp.ui.screens.informationScreen.SelectedCharacter

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
            val rickAndMortyViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)

            HomeScreen(
                rickAndMortyViewModel.appUiState,
                navigateToInformation = {
                    navController.navigate("${SelectedCharacter.route}/$it")
                }
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
                characterUiState = informationViewModel.characterUiState
            )
        }

        /*composable(
            route = SelectedCharacter.routeWithArgs,
            arguments = listOf(navArgument(SelectedCharacter.itemIdArg) { type = NavType.StringType })
        ) {
            val informationViewModel: InformationViewModel

            InformationScreen(
                navigationBack = { navController.navigateUp() },
                //characterUiState = informationViewModel.characterUiState
            )
        }*/
    }
}


@Composable
fun RickAndMortyNavHost2(
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
                rickAndMortyViewModel.appUiState,
                navigateToInformation = {
                    navController.navigate("${SelectedCharacter.route}/$it")
                }
            )
        }

        composable(
            route = SelectedCharacter.routeWithArgs,
            arguments = listOf(navArgument(SelectedCharacter.itemIdArg) { type = NavType.StringType })
        ) { backStackEntry -> // Добавляем экземпляр 'backStackEntry'
            val informationViewModel: InformationViewModel = viewModel() // получаем экземпляр InformationViewModel
            val itemId = backStackEntry.arguments?.getString(SelectedCharacter.itemIdArg) ?: "" // получаем id персонажа

            //informationViewModel.setCharacterUiState(itemId) // устанавливаем characterUiState в InformationViewModel

            /*InformationScreen(
                navigationBack = { navController.navigateUp() },
                //characterUiState = informationViewModel.characterUiState
            )*/
        }
    }
}
