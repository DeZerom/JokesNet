package ru.dezerom.jokesnet.screens.auth.login

sealed class LoginState {
    data class WaitingCredentials(val login: String, val pass: String) : LoginState()
    object WrongCredentials : LoginState()
    object CheckingCredentials : LoginState()
    object Success: LoginState()
}
