package ru.dezerom.jokesnet.screens.auth.registration

import ru.dezerom.jokesnet.screens.Event

sealed class RegistrationScreenEvent(private val whatToDo: () -> Unit = Event.DO_NOTHING): Event {
    class CreateNewUserEvent(val credentials: RegistrationState.CreatingCredentials)
        : RegistrationScreenEvent()
    class TryAgainWhenUnknownErrorEvent(val credentials: RegistrationState.CreatingCredentials)
        : RegistrationScreenEvent()
    class MalformedEmailEvent(val credentials: RegistrationState.CreatingCredentials, whatToDo: () -> Unit)
        : RegistrationScreenEvent(whatToDo)
    class WeakPasswordEvent(val credentials: RegistrationState.CreatingCredentials, whatToDo: () -> Unit)
        : RegistrationScreenEvent(whatToDo)
    class EmailChangedEvent(val newEmail: String): RegistrationScreenEvent()
    class PasswordChangedEvent(val newPassword: String): RegistrationScreenEvent()
    class LoginChangedEvent(val newLogin: String): RegistrationScreenEvent()

    override fun obtainEvent() = whatToDo()
}