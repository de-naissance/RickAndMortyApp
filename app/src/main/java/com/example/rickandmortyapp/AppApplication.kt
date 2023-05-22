package com.example.rickandmortyapp

import android.app.Application
import com.example.rickandmortyapp.data.AppContainer
import com.example.rickandmortyapp.data.DefaultAppContainer

class AppApplication : Application() {

    /**
     * Экземпляр AppContainer, используемый остальными классами для получения зависимостей
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}