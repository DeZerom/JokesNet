package ru.dezerom.jokesnet.screens.profile

sealed class ProfileScreenState {
    open class ShowingProfile(val profileInfo: ProfileInfo): ProfileScreenState()
    class EditingLogin(val newLogin: String, profileInfo: ProfileInfo): ShowingProfile(profileInfo)
    object Error: ProfileScreenState()
    object Loading: ProfileScreenState()
}
