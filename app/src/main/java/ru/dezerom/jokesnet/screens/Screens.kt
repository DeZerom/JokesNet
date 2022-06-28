package ru.dezerom.jokesnet.screens

enum class Screens {
    LOGIN, REGISTRATION, PROFILE;

    fun route(): String {
        return when (this) {
            LOGIN -> "login"
            REGISTRATION -> "registration"
            PROFILE -> "profile"
        }
    }
}