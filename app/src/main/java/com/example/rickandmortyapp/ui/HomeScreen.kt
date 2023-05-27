package com.example.rickandmortyapp.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.network.Location
import com.example.rickandmortyapp.network.Origin
import com.example.rickandmortyapp.network.ResultCharacter
import com.example.rickandmortyapp.ui.navigation.NavigationDestination
import java.util.Locale

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}
@Composable
fun HomeScreen(
    appUiState: AppUiState,
    navigateToInformation: (Int) -> Unit,
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
                navigateToInformation = navigateToInformation,
                characterList = appUiState.characterRequest,
                modifier.padding(innerPadding)
            )
            is AppUiState.Error -> ErrorScreen(modifier.padding(innerPadding))
        }

    }

    /** Попробовать потом реализовать использование DataStore, чтобы менять список на LazyGrid*/

}

@Composable
fun CardScreen(
    navigateToInformation: (Int) -> Unit,
    characterList: List<ResultCharacter>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(6.dp),

        ) {
        items(
            items = characterList
        ) {
                character -> CharacterCard(
            navigateToInformation = navigateToInformation,
            character = character,
            modifier = Modifier
        )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterCard(
    navigateToInformation: (Int) -> Unit,
    character: ResultCharacter,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { navigateToInformation(character.id) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatusInformation(
                id = character.id,
                name = character.name,
                status = character.status,
                gender = character.gender,
                modifier = modifier.fillMaxWidth(0.70f)
            )

            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(character.image)
                    .crossfade(true)
                    .build(),
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.loading_img),
                contentDescription = character.name,
                modifier = modifier,
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun StatusInformation(
    id: Int,
    name: String,
    status: String,
    gender: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .padding(start = 8.dp, top = 10.dp, end = 5.dp)
    ) {
        Text(
            text = String.format("#%03d", id),
            modifier = Modifier.padding(top = 4.dp)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = name
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatusIcon(status = status)
            Text(text = " - $gender")
        }
    }
}

@Composable
fun StatusIcon(
    status: String
) {
    /** Выбора цвета от статуса персонажа */
    val tint by animateColorAsState(
        when(status) {
            "Alive" -> Color(0xFF00BC00)
            "Dead" -> Color(0xFFFF0000)
            else -> Color(0xFFBCBCBC)
        }
    )
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .background(tint),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = status
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
            color = Color.White,
            modifier = Modifier.padding(3.dp)
        )
    }
}

@Preview
@Composable
fun TestHome() {
    val listCharacter = List(10
    ) {
        ResultCharacter(
            id = 10,
            name = "Toxic Rick",
            status = "Dead",
            species = "Humanoid",
            type = "Rick's Toxic Side",
            gender = "Male",
            origin = Origin("Alien Spa", "https://rickandmortyapi.com/api/location/64"),
            location = Location("Earth", "https://rickandmortyapi.com/api/location/20"),
            image = "https://rickandmortyapi.com/api/character/avatar/361.jpeg",
            episode = listOf("https://rickandmortyapi.com/api/episode/27"),
            url = "https://rickandmortyapi.com/api/character/361",
            created = "2018-01-10T18:20:41.703Z"
        )
    }
    CardScreen(
        characterList = listCharacter,
        navigateToInformation = { }
    )
}