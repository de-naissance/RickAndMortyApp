package com.example.rickandmortyapp.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortyapp.data.AppRepository
import com.example.rickandmortyapp.network.CharacterRequest
import com.example.rickandmortyapp.network.ResultCharacter
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

sealed interface AppUiState {
    data class Success(val characterRequest: CharacterRequest) : AppUiState
    object Error : AppUiState
    object Loading : AppUiState
}
class HomeViewModel(
    private val appRepository: AppRepository
): ViewModel() {

    /**
     * Изменяемое состояние, в котором хранится статус самого последнего запроса
     */
    var appUiState: AppUiState by mutableStateOf(AppUiState.Loading)
        private set

    init {
        getCharacter()
    }

    private fun getCharacter() {
        viewModelScope.launch {
            appUiState = AppUiState.Loading
            appUiState = try {
                AppUiState.Success(appRepository.getCharacter())
            } catch (e: IOException) {
                AppUiState.Error
            } catch (e: HttpException) {
                AppUiState.Error
            }
        }
    }

}

