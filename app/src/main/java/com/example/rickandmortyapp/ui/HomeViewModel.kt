package com.example.rickandmortyapp.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.data.AppRepository
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
     * Изменяемое состояние, в котором хранится статус самого последнего запроса
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
    var currentPage by mutableIntStateOf (1)
    var maxPage by mutableIntStateOf (1)
    /*
     * [selectLayout] изменяет макет и значки соответствующим образом и
     * сохраните выбранное в хранилище данных через  [userPreferencesRepository]
     */
    fun selectLayout(isLinearLayout: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveLayoutPreference(isLinearLayout)
        }
    }

    init {
        getCharacter()
    }
    fun getCharacter(
        page: Int? = currentPage
    ) {
        viewModelScope.launch {
            appUiState = AppUiState.Loading
            appUiState = try {
                val characterRequest = appRepository.getCharacter(
                    page = page
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