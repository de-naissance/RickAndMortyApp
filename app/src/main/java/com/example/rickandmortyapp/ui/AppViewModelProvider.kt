package com.example.rickandmortyapp.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.rickandmortyapp.AppApplication
import com.example.rickandmortyapp.ui.screens.HomeViewModel

/*
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AppApplication)
            val appRepository = application.container.appRepository
            HomeViewModel(appRepository = appRepository)
        }
    }
}*/
