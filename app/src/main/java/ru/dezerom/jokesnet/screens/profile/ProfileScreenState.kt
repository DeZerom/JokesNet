package ru.dezerom.jokesnet.screens.profile

sealed class ProfileScreenState {
    class ShowingProfile(val profileInfo: ProfileInfo): ProfileScreenState()
    object Error: ProfileScreenState()
    object Loading: ProfileScreenState()
}
