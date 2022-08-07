package ru.dezerom.jokesnet.screens.auth.login

sealed class LoginScreenState {
    data class WaitingCredentials(val email: String, val pass: String) : LoginScreenState()
    class WrongCredentials(val credentials: WaitingCredentials) : LoginScreenState()
    object CheckingCredentials : LoginScreenState()
    object Success: LoginScreenState()
    object CheckingToken : LoginScreenState()
    class UnknownErrorState(val credentials: WaitingCredentials) : LoginScreenState()
}
