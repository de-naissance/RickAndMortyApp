package com.example.rickandmortyapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.network.ResultCharacter
import com.example.rickandmortyapp.ui.RickAndMortyTopAppBar
import com.example.rickandmortyapp.ui.navigation.NavigationDestination

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}
@Composable
fun HomeScreen(
    appUiState: AppUiState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            RickAndMortyTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false
            )
        }
    ) { innerPadding ->
        when (appUiState) {
            is AppUiState.Loading -> LoadingScreen(modifier.padding(innerPadding))
            is AppUiState.Success -> CardScreen(
                characterList = appUiState.characterRequest.resultCharacters
            )
            is AppUiState.Error -> ErrorScreen(modifier.padding(innerPadding))
        }
    }

/** Попробовать потом реализовать использование DataStore, чтобы менять список на LazyGrid*/

}

@Composable
fun CardScreen(characterList: List<ResultCharacter>) {
    LazyColumn() {
        items(
            items = characterList
        ) {
            character -> CharacterCard(character = character)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterCard(
    character: ResultCharacter,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { /*TODO*/ },
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row() {
            Column {
                Text(text = character.name)
            }
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(character.image)
                    .crossfade(true)
                    .build(),
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.loading_img),
                contentDescription = character.name,

            )
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.loading_img),
            contentDescription = stringResource(id = R.string.loading),
            modifier = Modifier.size(200.dp)
        )
    }
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(stringResource(R.string.loading_failed))
    }
}