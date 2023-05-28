package com.example.rickandmortyapp.network

data class ResultCharacter(
    /** Время создания персонажа в базе данных */
    val created: String,
    /** Список серий, в которых появлялся этот персонаж */
    val episode: List<String>,
    /** Пол персонажа («Женский», «Мужской», «Бесполый» или «Неизвестно») */
    val gender: String,
    /** Идентификатор персонажа */
    val id: Int,
    /** Ссылка на изображение персонажа 300x300 пикселей */
    val image: String,
    /** Имя и ссылка на последнюю известную конечную точку местоположения персонажа */
    val location: Location,
    /** Имя персонажа */
    val name: String,
    /** Имя и ссылка на место происхождения персонажа */
    val origin: Origin,
    /** Вид персонажа */
    val species: String,
    /** Статус персонажа («Живой», «Мертвый» или «Неизвестный») */
    val status: String,
    /** Тип или подвид персонажа */
    val type: String,
    /** Ссылка на собственную конечную точку URL персонажа */
    val url: String
)