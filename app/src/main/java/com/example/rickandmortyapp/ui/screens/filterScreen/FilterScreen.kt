package com.example.rickandmortyapp.ui.screens.filterScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.data.SearchFilter
import com.example.rickandmortyapp.ui.HomeViewModel
import com.example.rickandmortyapp.ui.InformationScreenTopAppBar
import com.example.rickandmortyapp.ui.navigation.NavigationDestination

object SearchDestination : NavigationDestination {
    override val route = "filterCharacters"
    override val titleRes = R.string.filterCharacters
}

@Composable
fun FilterScreen(
    navigationBack: () -> Unit,
    viewModel: HomeViewModel,
    onSendButtonClicked: () -> Unit
) {
    var searchUiState = viewModel.searchFilter

    Scaffold(
        topBar = {
            InformationScreenTopAppBar(
                title = SearchDestination.route,
                canNavigateBack = true,
                navigateUp = navigationBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            FilterTextField(
                search = searchUiState.name,
                changeSearch = { searchUiState = searchUiState.copy(name = it) },
                label = "Character"
            )
            FilterGivenStatus(
                _selected = searchUiState.status,
                changeSelection = { searchUiState = searchUiState.copy(status = it) }
            )
            FilterTextField(
                search = searchUiState.species,
                changeSearch = { searchUiState = searchUiState.copy(species = it) },
                label = "Species"
            )
            FilterTextField(
                search = searchUiState.type,
                changeSearch = { searchUiState = searchUiState.copy(type = it) },
                label = "Type"
            )
            FilterGivenGender(
                _selected = searchUiState.gender,
                changeSelection = { searchUiState = searchUiState.copy(gender = it) }
            )
            ResultButton(
                searchFilter = {
                    viewModel.filterChange(searchUiState)
                    onSendButtonClicked()
                },
                reset = { searchUiState = SearchFilter() }
            )
        }
    }
}

/**
 * Field for entering and changing filter parameters in text format
 */
@Composable
fun FilterTextField(
    search: String,
    changeSearch: (String) -> Unit,
    label: String
) {
    var searchText by rememberSaveable { mutableStateOf(search) }

    OutlinedTextField(
        value = searchText,
        onValueChange = {
            searchText = it
            changeSearch(searchText)
        },
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ),
        shape = MaterialTheme.shapes.small,
        trailingIcon = {
            CancelInput(
                searchText = {
                    searchText = ""
                    changeSearch(searchText)
                }
            )
        }
    )
}

// Selecting a status parameter
@Composable
fun FilterGivenStatus(
    _selected: String,
    changeSelection: (String) -> Unit
) {
    val option = listOf("All", "Alive", "Dead", "Unknown")
    val selected by remember { mutableStateOf(if (_selected == "") "All" else _selected) }
    var state by remember { mutableIntStateOf(option.indexOfFirst { it == selected }) }

    TabRow(
        selectedTabIndex = state,
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .clip(MaterialTheme.shapes.small)
    ) {
        option.forEachIndexed { index, status ->
            Tab(
                selected = state == index,
                onClick =  {
                    state = index
                    changeSelection(if (status == "All") "" else status)
                },
                text = {
                    Text(
                        text = status,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                modifier = Modifier.clip(MaterialTheme.shapes.medium)
            )
        }
    }
}

// Selecting the gender parameter
@Composable
fun FilterGivenGender(
    _selected: String,
    changeSelection: (String) -> Unit
) {
    val option = listOf("All", "Female", "Male", "Genderless", "Unknown")
    val selected by remember { mutableStateOf(if (_selected == "") "All" else _selected) }
    var state by remember { mutableIntStateOf(option.indexOfFirst { it == selected }) }

    ScrollableTabRow(
        selectedTabIndex = state,
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .clip(MaterialTheme.shapes.small)
    ) {
        option.forEachIndexed { index, status ->
            Tab(
                selected = state == index,
                onClick =  {
                    state = index
                    changeSelection(if (status == "All") "" else status)
                },
                text = {
                    Text(
                        text = status,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                modifier = Modifier.clip(MaterialTheme.shapes.medium)
            )
        }
    }
}

@Composable
fun CancelInput(
    searchText: () -> Unit
) {
    IconButton(
        onClick = searchText

    ) {
        Icon(
            painter = painterResource(id = R.drawable.cancel),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}


// Return to HomeScreen with set or reset parameters
@Composable
fun ResultButton(
    searchFilter: () -> Unit,
    reset: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = searchFilter,
            modifier = Modifier.padding(horizontal = 6.dp)
        ) {
            Text(text = "Search")
        }
        Button(
            onClick = {
                reset()
                searchFilter()
            } ,
            modifier = Modifier.padding(horizontal = 6.dp)
        ) {
            Text(text = "Reset")
        }
    }
}