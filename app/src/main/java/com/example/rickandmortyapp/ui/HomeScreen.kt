package com.example.rickandmortyapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.network.Location
import com.example.rickandmortyapp.network.Origin
import com.example.rickandmortyapp.network.ResultCharacter
import com.example.rickandmortyapp.ui.components.StatusIcon
import com.example.rickandmortyapp.ui.navigation.NavigationDestination
import com.example.rickandmortyapp.ui.theme.RickAndMortyAppTheme

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}
@Composable
fun HomeScreen(
    appUiState: AppUiState,
    navigateToInformation: (Int) -> Unit,
    navigateToSearch: () -> Unit,
    selectLayout: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    layoutUiState: LayoutUiState
) {
    Scaffold(
        topBar = {
            HomeTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                layoutUiState = layoutUiState,
                selectLayout = selectLayout,
                navigateToSearch = navigateToSearch
            )
        }
    ) { innerPadding ->
        when (appUiState) {
            is AppUiState.Loading -> LoadingScreen(modifier.padding(innerPadding))
            is AppUiState.Success -> CharacterScreen(
                navigateToInformation = navigateToInformation,
                characterList = appUiState.characterRequest,
                isLinearLayout = layoutUiState.isLinearLayout,
                modifier = modifier.padding(innerPadding)
            )
            is AppUiState.Error -> ErrorScreen(modifier.padding(innerPadding))
        }

    }
}

@Composable
fun CharacterScreen(
    navigateToInformation: (Int) -> Unit,
    characterList: List<ResultCharacter>,
    isLinearLayout: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        TabPage()
        if (isLinearLayout) {
            CardScreen(
                navigateToInformation = navigateToInformation,
                characterList = characterList
            )
        } else {
            GridScreen(
                navigateToInformation = navigateToInformation,
                characterList = characterList
            )
        }
    }
}

@Composable
fun TabPage(
    viewModel: HomeViewModel = viewModel()
) {
    val state = viewModel.currentPage
    val lst = List(viewModel.maxPage) { it + 1 }

    ScrollableTabRow(
        selectedTabIndex = state - 1,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .clip(MaterialTheme.shapes.small)
    ) {
        lst.forEachIndexed { index, title ->
            Tab(
                selected = state - 1 == index,
                onClick = {
                    viewModel.currentPage = title
                    viewModel.getCharacter()
                },
                text = { Text(
                    text = title.toString(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis) },
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
            )
        }
    }
}

@Composable
fun CardScreen(
    navigateToInformation: (Int) -> Unit,
    characterList: List<ResultCharacter>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(6.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
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
}

@Composable
fun GridScreen(
    navigateToInformation: (Int) -> Unit,
    characterList: List<ResultCharacter>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(180.dp),
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(
            items = characterList,
            key = { character -> character.id }
        ) {character ->
            GridCharacterCard(
                navigateToInformation = navigateToInformation,
                character = character
            )
        }
    }
}

@Composable
fun GridCharacterCard(
    navigateToInformation: (Int) -> Unit,
    character: ResultCharacter,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.BottomStart,
        modifier = modifier
            .padding(6.dp)
            .clickable { navigateToInformation(character.id) }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(character.image)
                .crossfade(true)
                .build(),
            error = painterResource(id = R.drawable.ic_broken_image),
            placeholder = painterResource(id = R.drawable.loading_img),
            contentDescription = character.name,
            modifier = modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.small),
            contentScale = ContentScale.Crop
        )
        Text(
            text = character.name,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                ),
            style = TextStyle(
                color = MaterialTheme.colorScheme.surfaceVariant
            )
        )
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

@Preview
@Composable
fun TestGridScreen() {
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
    GridScreen(
        characterList = listCharacter,
        navigateToInformation = { }
    )
}
@Preview
@Composable
fun TestDarkGridScreen() {
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
        GridScreen(
            characterList = listCharacter,
            navigateToInformation = { }
        )
    }
}
