package com.example.rickandmortyapp.ui.screens.informationScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.network.Episode
import com.example.rickandmortyapp.network.Location
import com.example.rickandmortyapp.network.Origin
import com.example.rickandmortyapp.network.ResultCharacter
import com.example.rickandmortyapp.ui.ErrorScreen
import com.example.rickandmortyapp.ui.InformationScreenTopAppBar
import com.example.rickandmortyapp.ui.LoadingScreen
import com.example.rickandmortyapp.ui.components.LoadingEpisodeCard
import com.example.rickandmortyapp.ui.components.StatusIcon
import com.example.rickandmortyapp.ui.navigation.NavigationDestination
import com.example.rickandmortyapp.ui.theme.RickAndMortyAppTheme

object SelectedCharacter : NavigationDestination {
    override val route = "CharacterInformation"
    override val titleRes =  R.string.characterInformation
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@Composable
fun InformationScreen(
    navigationBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InformationViewModel = viewModel()
) {
    val characterUiState = viewModel.characterUiState
    Scaffold(
        topBar = {
            InformationScreenTopAppBar(
                title = SelectedCharacter.route,
                canNavigateBack = true,
                navigateUp = navigationBack
            )
        }
    ) { innerPadding ->
        when (characterUiState) {
            is CharacterUiState.Loading -> LoadingScreen(modifier.padding(innerPadding))
            is CharacterUiState.Success -> Information(
                modifier = modifier.padding(innerPadding),
                character = characterUiState.character
            )
            is CharacterUiState.Error -> ErrorScreen(modifier.padding(innerPadding))
        }
    }
}

@Composable
fun Information(
    modifier: Modifier = Modifier,
    character: ResultCharacter
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(character.image)
                .crossfade(true)
                .build(),
            error = painterResource(id = R.drawable.ic_broken_image),
            placeholder = painterResource(id = R.drawable.loading_img),
            contentDescription = character.name,
            contentScale = ContentScale.FillHeight,
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f),
            alignment = Alignment.Center
        )
        CardInformation(
            character = character
        )
    }
}

/**
 * Displaying information about the character using TabRow
 * and smooth animation of the transition between tabs
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CardInformation(
    character: ResultCharacter,
    modifier: Modifier = Modifier
) {
    var state by remember { mutableIntStateOf(0) }
    val titles = listOf("About", "Episodes")

    Card(
        modifier = modifier
            .fillMaxSize(1f)
            .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ){
        Column(
            modifier = modifier.padding(top = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Available character information",
                    modifier = Modifier,
                )
            }
            TabRow(
                selectedTabIndex = state,
                modifier = modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        selected = state == index,
                        onClick = { state = index },
                        text = { Text(text = title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                    )
                }
            }
            AnimatedContent(
                targetState = state,
                transitionSpec = {
                    if (targetState == 0) {
                        slideInHorizontally { width -> -width } + fadeIn() with
                                slideOutHorizontally { width -> width } + fadeOut()
                    } else {
                        slideInHorizontally { width -> width } + fadeIn() with
                                slideOutHorizontally { width -> -width } + fadeOut()
                    }.using(SizeTransform(clip = false))
                },
                modifier = Modifier.padding(top = 12.dp)
            ) {targetIndex ->
                when(targetIndex) {
                    0 -> AboutTab(
                        status = character.status,
                        gender = character.gender,
                        species = character.species,
                        type = character.type,
                        origin = character.origin,
                        location = character.location
                    )
                    1 -> EpisodeInformation(character.episode)
                }
            }
        }
    }
}

/**
 * Character Tabular information
 */
@Composable
fun AboutTab(
    status: String,
    gender: String,
    species: String,
    type: String,
    origin: Origin,
    location: Location
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.status)
            )
            StatusIcon(status = status)
        }
        Divider(modifier = Modifier.padding(vertical = 6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(id = R.string.gender))
            Text(text = gender)
        }
        Divider(modifier = Modifier.padding(vertical = 6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(id = R.string.species))
            Text(text = species)
        }
        if (type.isNotEmpty()) {
            Divider(modifier = Modifier.padding(vertical = 6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(id = R.string.type))
                Text(text = type)
            }
        }
        Divider(modifier = Modifier.padding(vertical = 6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(id = R.string.origin))
            Text(text = origin.name)
        }
        Divider(modifier = Modifier.padding(vertical = 6.dp))
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.lastLocation))
            Text(text = location.name)
        }
        Divider(modifier = Modifier.padding(vertical = 6.dp))
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.firstSeenIn))
            Text(text = location.name)
        }
    }
}

/**
 * The output of the episodes in which the character played.
 * If the episode is loaded, the [LoadingEpisodeCard] stub is displayed
 */
@Composable
fun EpisodeInformation(
    episodes: List<String>,
    viewModel: InformationViewModel = viewModel()
) {
    LazyColumn {
        items(episodes) {url ->
            val episodeState = viewModel.getEpisode(url).observeAsState()
            var episode by remember { mutableStateOf<Episode?>(null) }

            LaunchedEffect(episodeState.value) {
                episode = episodeState.value
            }

            if (episode != null) {
                EpisodeCard(episode = episode!!)
            } else {
                LoadingEpisodeCard(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

/**
 * Episode Information
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeCard(
    episode: Episode
) {
    Card(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = episode.episode)
            Text(text = episode.name)
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun TestHomeDark() {
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
    RickAndMortyAppTheme(useDarkTheme = true) {
        Information(
            character = listCharacter[0]
        )
    }
}
@Preview(
    showBackground = true,
    showSystemUi = true,
)
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
    RickAndMortyAppTheme(useDarkTheme = false) {
        Information(
            character = listCharacter[0]
        )
    }
}