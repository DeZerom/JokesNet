package ru.dezerom.jokesnet.screens

interface Event {
    fun obtainEvent()

    companion object {
        val DO_NOTHING = {}
    }
}