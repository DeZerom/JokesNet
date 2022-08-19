package ru.dezerom.jokesnet.screens.joke_view

sealed class JokeScreenState {
    class PresentingJokeState(val joke: Joke): JokeScreenState()
    object NoJokeProvidedYet: JokeScreenState()
    object ErrorState: JokeScreenState()
    object LoadingState: JokeScreenState()
}
