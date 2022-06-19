package ru.dezerom.jokesnet.screens.auth.registration

sealed class RegistrationState {
    data class CreatingCredentials(val login: String, val pass: String): RegistrationState()
    object Loading: RegistrationState()
    object AccountExists: RegistrationState()
    object Success : RegistrationState()
}