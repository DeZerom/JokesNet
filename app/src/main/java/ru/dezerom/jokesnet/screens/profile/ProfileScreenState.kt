package ru.dezerom.jokesnet.screens.profile

sealed class ProfileScreenState {
    class ShowingProfile(val login: String): ProfileScreenState()
    object Error: ProfileScreenState()
    object Loading: ProfileScreenState()
}
