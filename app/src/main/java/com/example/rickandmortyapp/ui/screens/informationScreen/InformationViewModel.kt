package com.example.rickandmortyapp.ui.screens.informationScreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.rickandmortyapp.data.AppRepository
import com.example.rickandmortyapp.network.Episode
import com.example.rickandmortyapp.network.ResultCharacter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

sealed interface CharacterUiState {
    data class Success(val character: ResultCharacter) : CharacterUiState
    object Error : CharacterUiState
    object Loading : CharacterUiState
}

class InformationViewModel(
    savedStateHandle: SavedStateHandle,
    private val appRepository: AppRepository
) : ViewModel() {

    private val idCharacter: Int =
        checkNotNull(savedStateHandle[SelectedCharacter.itemIdArg])


    var characterUiState: CharacterUiState by mutableStateOf(CharacterUiState.Loading)
        private set

    init {
        getCharacter()
    }

    private fun getCharacter() {
        viewModelScope.launch {
            characterUiState = CharacterUiState.Loading
            characterUiState = try {
                val character = appRepository.getSelectedCharacter(id = idCharacter)
                CharacterUiState.Success(character)
            } catch (e: IOException) {
                CharacterUiState.Error
            } catch (e: HttpException) {
                CharacterUiState.Error
            }
        }
    }

    fun getEpisode(url: String): LiveData<Episode> {
        val id = url.substring(url.lastIndexOf('/') + 1).toInt()
        val episodeLiveData = MutableLiveData<Episode>()

        viewModelScope.launch {
            try {
                val episode = appRepository.getEpisode(id = id)
                episodeLiveData.value = episode
            } catch (e: IOException) {
                Log.e("getEpisode", "IOException")
            } catch (e: HttpException) {
                Log.e("getEpisode", "HttpException")
            }
        }
        return episodeLiveData
    }
}

