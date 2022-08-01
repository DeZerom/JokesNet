package ru.dezerom.jokesnet.screens.auth.registration

sealed class RegistrationState {
    data class CreatingCredentials(val email: String, val login: String, val pass: String): RegistrationState()
    object Loading: RegistrationState()
    object AccountExists: RegistrationState()
    class WeakPassword(val credentials: CreatingCredentials): RegistrationState()
    class MalformedEmail(val credentials: CreatingCredentials): RegistrationState()
    class UnknownError(val credentials: CreatingCredentials): RegistrationState()
    object Success : RegistrationState()
}