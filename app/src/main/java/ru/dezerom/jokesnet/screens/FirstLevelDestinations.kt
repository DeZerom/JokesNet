package ru.dezerom.jokesnet.screens

enum class FirstLevelDestinations {
    LOGIN, REGISTRATION, NESTED_SCREENS;

    fun route(): String {
        return when (this) {
            LOGIN -> "login"
            REGISTRATION -> "registration"
            NESTED_SCREENS -> "nested_screens"
        }
    }
}