package ru.dezerom.jokesnet.screens.joke_view

import ru.dezerom.jokesnet.screens.Event

sealed class JokeScreenEvent(private val whatToDo: () -> Unit = Event.DO_NOTHING): Event {
    object RandomJokeRequested: JokeScreenEvent()
    object NewJokeRequested: JokeScreenEvent()

    override fun obtainEvent() = whatToDo()
}
