package com.example.rickandmortyapp.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.rickandmortyapp.AppApplication
import com.example.rickandmortyapp.ui.screens.informationScreen.InformationViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            val appRepository = appApplication().container.appRepository
            HomeViewModel(appRepository = appRepository)
        }

        initializer {
            InformationViewModel(
                this.createSavedStateHandle(),
                appApplication().container.appRepository
            )
        }
    }
}

fun CreationExtras.appApplication(): AppApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AppApplication)
