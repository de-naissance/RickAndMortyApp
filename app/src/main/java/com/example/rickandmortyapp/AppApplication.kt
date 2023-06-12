package com.example.rickandmortyapp

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.rickandmortyapp.data.AppContainer
import com.example.rickandmortyapp.data.DefaultAppContainer
import com.example.rickandmortyapp.data.UserPreferencesRepository

private const val LAYOUT_PREFERENCE_NAME = "layout_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = LAYOUT_PREFERENCE_NAME
)
private const val DARK_MODE_PREFERENCE_NAME = "dark_mode_preferences"

class AppApplication : Application() {
    lateinit var userPreferencesRepository: UserPreferencesRepository

    /**
     * Экземпляр AppContainer, используемый остальными классами для получения зависимостей
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }
}