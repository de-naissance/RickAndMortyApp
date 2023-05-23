package com.example.rickandmortyapp.ui.navigation

/**
 * Интерфейс для описания пунктов назначения навигации для приложения
 */
interface NavigationDestination {
    /**
     * Уникальное имя для определения пути к составному элементу
     */
    val route: String

    /**
     * Строковый идентификатор ресурса, содержащий заголовок, который будет отображаться на экране.
     */
    val titleRes: Int
}