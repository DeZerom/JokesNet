package ru.dezerom.jokesnet.screens.joke_add

import android.widget.Toast
import ru.dezerom.jokesnet.screens.Event

sealed class JokeAddScreenEvent(private val whatToDo: () -> Unit): Event {
    /**
     * Should be used when text of a joke is changed
     */
    class JokeTextChangedEvent(val newText: String): JokeAddScreenEvent(Event.DO_NOTHING)

    /**
     * Should be called when user clicks on "add joke button"
     */
    class JokeAddedEvent(val toast: Toast, whatToDo: () -> Unit = { toast.show() }): JokeAddScreenEvent(whatToDo)

    /**
     * Should be called when user clicks on "try again button"
     */
    object TryAgainEvent: JokeAddScreenEvent(Event.DO_NOTHING)

    override fun obtainEvent() = whatToDo()
}
