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
import com.example.rickandmortyapp.ui.HomeViewModel
import com.example.rickandmortyapp.ui.InformationScreenTopAppBar
import com.example.rickandmortyapp.ui.navigation.NavigationDestination

object SearchDestination : NavigationDestination {
    override val route = "search"
    override val titleRes = R.string.app_name
}

@Composable
fun FilterScreen(
    navigationBack: () -> Unit,
    viewModel: HomeViewModel,
    onSendButtonClicked: () -> Unit
) {
    val searchUiState = viewModel.filterSearch.value
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
            FilterGivenName(searchUiState.name)
            FilterGivenStatus(
                searchUiState.status,
                change = { searchUiState.status = it }
            )
            FilterGivenSpecies(searchUiState.species)
            FilterGivenType(searchUiState.type)
            FilterGivenGender(searchUiState.gender)
            ResultButton(
                searchFilter = {
                    viewModel.filterChange(searchUiState)
                    onSendButtonClicked()
                }
            )
        }
    }
}

@Composable
fun FilterGivenName(search: String) {
    var searchText by rememberSaveable { mutableStateOf(search) }

    OutlinedTextField(
        value = searchText,
        onValueChange = { searchText = it },
        label = { Text("Character") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ),
        shape = MaterialTheme.shapes.small,
        trailingIcon = { CancelInput(searchText = { searchText = "" }) }
    )
}

@Composable
fun FilterGivenStatus(
    stat: String,
    change: (String) -> Unit
) {
    val status = listOf("All", "Alive", "Dead", "Unknown")
    val s = status.indexOfFirst { it.lowercase() == stat }
    var state by remember { mutableIntStateOf(if (s == -1) 0 else s) }

    TabRow(
        selectedTabIndex = state,
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .clip(MaterialTheme.shapes.small)
    ) {
        status.forEachIndexed { index, status ->
            Tab(
                selected = state == index,
                onClick =  {
                    state = index
                    change(if (status == "All") "" else status)
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
fun FilterGivenSpecies(
    search: String
) {
    var searchText by rememberSaveable { mutableStateOf(search) }

    OutlinedTextField(
        value = searchText,
        onValueChange = { searchText = it },
        label = { Text("Species") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ),
        shape = MaterialTheme.shapes.small,
        trailingIcon = { CancelInput(searchText = { searchText = "" }) }
    )
}
@Composable
fun FilterGivenType(
    search: String
) {
    var searchText by rememberSaveable { mutableStateOf(search) }

    OutlinedTextField(
        value = searchText,
        onValueChange = { searchText = it },
        label = { Text("Type") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ),
        shape = MaterialTheme.shapes.small,
        trailingIcon = { CancelInput(searchText = { searchText = "" }) }
    )
}

@Composable
fun FilterGivenGender(
    gender: String
) {
    var state by remember { mutableIntStateOf(0) }
    val status = listOf("All", "Female", "Male", "Genderless", "Unknown")

    ScrollableTabRow(
        selectedTabIndex = state,
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .clip(MaterialTheme.shapes.small)
    ) {
        status.forEachIndexed { index, status ->
            Tab(
                selected = state == index,
                onClick =  { state = index },
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

@Composable
fun ResultButton(
    searchFilter: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
        ,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = searchFilter,
            modifier = Modifier.padding(horizontal = 6.dp)
        ) {
            Text(text = "Search")
        }
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.padding(horizontal = 6.dp)
        ) {
            Text(text = "Reset")
        }
    }
}