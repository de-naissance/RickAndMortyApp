package com.example.rickandmortyapp.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.data.AppRepository
import com.example.rickandmortyapp.data.SearchFilter
import com.example.rickandmortyapp.data.UserPreferencesRepository
import com.example.rickandmortyapp.network.ResultCharacter
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

sealed interface AppUiState {
    data class Success(val characterRequest: List<ResultCharacter>) : AppUiState
    object Error : AppUiState
    object Loading : AppUiState
}
class HomeViewModel(
    private val appRepository: AppRepository,
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    /**
     * The changeable state in which the status of the most recent request is stored
     */
    var appUiState: AppUiState by mutableStateOf(AppUiState.Loading)
        private set
    val layoutUiState: StateFlow<LayoutUiState> =
        userPreferencesRepository.isLinearLayout.map { isLinearLayout ->
            LayoutUiState(isLinearLayout)
        }.stateIn(
            scope = viewModelScope,
            // Flow is set to emits value for when app is on the foreground
            // 5 seconds stop delay is added to ensure it flows continuously
            // for cases such as configuration change
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LayoutUiState()
        )
    // Current page number
    var currentPage by mutableIntStateOf (1)
    // Number of pages in the request
    var maxPage by mutableIntStateOf (1)
    companion object {
        // Search Filter Data
        private var _searchFilter by mutableStateOf(SearchFilter())
    }
    val searchFilter = _searchFilter

    /**
     * [selectLayout] changes the layout and icons accordingly and
     * save the selected to the data warehouse via [userPreferencesRepository]
     */
    fun selectLayout(isLinearLayout: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveLayoutPreference(isLinearLayout)
        }
    }

    init {
        getCharacter()
    }

    /**
     * A function that sends a request to display a list of characters, indicating the page.
     * The search parameters are taken from the [SearchFilter] variable
     */
    fun getCharacter(
        page: Int? = currentPage
    ) {
        viewModelScope.launch {
            appUiState = AppUiState.Loading
            appUiState = try {
                val characterRequest = appRepository.getCharacter(
                    page = page,
                    name = searchFilter.name,
                    status = searchFilter.status,
                    species = searchFilter.species,
                    type = searchFilter.type,
                    gender = searchFilter.gender
                )
                val characters = characterRequest.results
                maxPage = characterRequest.info.pages
                AppUiState.Success(characters)
            } catch (e: IOException) {
                AppUiState.Error
            } catch (e: HttpException) {
                AppUiState.Error
            }
        }
    }

    /**
     * Changing the search parameters and setting the current page to 1
     */
    fun filterChange(
        searchFilter: SearchFilter
    ) {
        currentPage = 1
        _searchFilter = searchFilter
    }
}

/*
 * Data class containing various Layout UI States for Home screen
 */
data class LayoutUiState(
    val isLinearLayout: Boolean = true,
    val toggleContentDescription: String =
        if (isLinearLayout) "Linear Layout" else "Grid Layout",
    val toggleIcon: Int =
        if (isLinearLayout) R.drawable.toc else R.drawable.grid_view
)