package ru.dezerom.jokesnet.screens.profile

import ru.dezerom.jokesnet.screens.Event

sealed class ProfileScreenEvent(private val whatToDo: () -> Unit = Event.DO_NOTHING): Event {
    /**
     * Event of querying profile info
     */
    object ProfileInfoQueried: ProfileScreenEvent()

    /**
     * Event of navigating out of [ProfileScreen]
     */
    class NavigatedOut(private val navEvent: () -> Unit): ProfileScreenEvent() {
        override fun obtainEvent() = navEvent()
    }

    /**
     * Event of trying again if error occurs
     */
    object TriedAgain: ProfileScreenEvent()

    class LoginChangeRequested(val info: ProfileScreenState.ShowingProfile): ProfileScreenEvent()

    class LoginChangeRequestCancelled(val info: ProfileScreenState.EditingLogin)
        : ProfileScreenEvent()

    class LoginChangeRequestAccepted(val info: ProfileScreenState.EditingLogin)
        : ProfileScreenEvent()

    class LoginChanged(val info: ProfileScreenState.EditingLogin): ProfileScreenEvent()

    override fun obtainEvent() = whatToDo()
}
