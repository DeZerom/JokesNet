package ru.dezerom.jokesnet.screens.profile

import ru.dezerom.jokesnet.screens.Event

sealed class ProfileScreenEvent: Event {
    /**
     * Event of querying profile info
     */
    object ProfileInfoQueried: ProfileScreenEvent() {
        override fun obtainEvent() {}
    }

    /**
     * Event of navigating out of [ProfileScreen]
     */
    class NavigateOut(private val navEvent: () -> Unit): ProfileScreenEvent() {
        override fun obtainEvent() = navEvent.invoke()
    }

    /**
     * Event of trying again if error occurs
     */
    object TryAgain: ProfileScreenEvent() {
        override fun obtainEvent() {}
    }
}
