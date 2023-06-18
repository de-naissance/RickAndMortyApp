# RickAndMortyApp

RickAndMortyApp - это приложение позволяет узнать информаицию о персонажах мультсериала Rick and Morty, использую для этого открытое API https://rickandmortyapi.com/

В приложение демонстрируется работа с:
* ViewModel
* Navigation
* Flow
* Coil
* Kotlin Coroutine
* Datastore
* Retrofit

# О проекте
Приложение на главном экране отображает краткую информацию о персонажах мультсериала. На одном странице может отображаться информация до 20 персонажей.

При выборе персонажа происходит переход на новоую страницу, на которой показана более полная информация в виле About/Episode выполненая с помощью TabRow с использованием плавной анимации перехода между вкладками. 

Если информация о эпизоде не была загружена, тогда вместо неё будет отображаться анимированная заглушка карточки эпизода.

На гланом экране имеется кнопка поиска, которая перенаправляет пользователя на экран фильтра, в которой можно указать нужные ограничения запроса. Они используют для этого текстовые поля и TabRow атрибуты.

Также на главном экране имеет возможность в поле TopBar выбирать вид карточек персонажей в виде Grid/Layout и выбор тёмной темы через DropMenu.

# Скриншоты
Светлая тема:
<p align="middle">
  <img src="./screenshots/Screenshot_1.png" width="270">
  <img src="./screenshots/Screenshot_2.png" width="270">
  <img src="./screenshots/Screenshot_3.png" width="270">
</p>

Тёмная тема:
<p align="middle">
  <img src="./screenshots/Screenshot_4.png" width="270">
  <img src="./screenshots/Screenshot_5.png" width="270">
  <img src="./screenshots/Screenshot_6.png" width="270">
</p>
