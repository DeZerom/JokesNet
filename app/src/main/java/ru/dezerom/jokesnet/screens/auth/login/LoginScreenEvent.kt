package ru.dezerom.jokesnet.screens.auth.login

import ru.dezerom.jokesnet.screens.Event

sealed class LoginScreenEvent(private val whatToDo: () -> Unit = Event.DO_NOTHING) : Event {
    class EmailChanged(val newEmail: String) : LoginScreenEvent()
    class PasswordChanged(val newPassword: String) : LoginScreenEvent()
    class LoginBtnClicked(val credentials: LoginScreenState.WaitingCredentials) : LoginScreenEvent()
    class RegistrationBtnClicked(whatToDo: () -> Unit) : LoginScreenEvent(whatToDo)
    class TryAgainBtnClickedWhenWrongCredentials(
        val credentials: LoginScreenState.WaitingCredentials
    ) : LoginScreenEvent()
    class SuccessLoginEvent(whatToDo: () -> Unit) : LoginScreenEvent(whatToDo)
    class TryAgainBtnClickedWhenUnknownError(
        val credentials: LoginScreenState.WaitingCredentials
    ) : LoginScreenEvent()

    override fun obtainEvent() = whatToDo()
}
