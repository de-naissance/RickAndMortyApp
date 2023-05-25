package com.example.rickandmortyapp.network

data class ResultCharacter(
    val created: String,    // Время создания персонажа в базе данных.
    val episode: List<String>,  // Список серий, в которых появлялся этот персонаж.
    val gender: String,     // Пол персонажа («Женский», «Мужской», «Бесполый» или «Неизвестно»).
    val id: Int,            // Идентификатор персонажа.
    val image: String,      // Ссылка на изображение персонажа 300x300 пикселей.
    val location: Location, // Имя и ссылка на последнюю известную конечную точку местоположения персонажа.
    val name: String,       // Имя персонажа.
    val origin: Origin,     // Имя и ссылка на место происхождения персонажа.
    val species: String,    // Вид персонажа.
    val status: String,     // Статус персонажа («Живой», «Мертвый» или «Неизвестный»).
    val type: String,       // Тип или подвид персонажа.
    val url: String         // Ссылка на собственную конечную точку URL персонажа.
)