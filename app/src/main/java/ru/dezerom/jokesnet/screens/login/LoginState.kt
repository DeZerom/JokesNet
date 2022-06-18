package ru.dezerom.jokesnet.screens.login

sealed class LoginState {
    data class WaitingCredentials(val login: String, val pass: String) : LoginState()
    object WrongCredentials : LoginState()
    object CheckingCredentials : LoginState()
}
