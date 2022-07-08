package ru.dezerom.jokesnet.screens.joke_add

sealed class JokeAddScreenState {
    class AddingJokeState(val jokeText: String): JokeAddScreenState()
    object LoadingState: JokeAddScreenState()
    class ErrorState(val jokeText: String): JokeAddScreenState()
}
