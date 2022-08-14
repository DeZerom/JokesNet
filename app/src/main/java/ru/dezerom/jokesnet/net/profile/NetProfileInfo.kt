package ru.dezerom.jokesnet.net.profile

import ru.dezerom.jokesnet.screens.profile.ProfileInfo

/**
 * Representing user's profile info. This class is needed ONLY for Firestore deserialization. In
 * other cases use [ProfileInfo]
 */
class NetProfileInfo {
    var login: String = ""
    var email: String = ""
    var jokesAdded: Int = 0

    /**
     * Return equivalent [ProfileInfo] instance
     */
    fun toProfileInfo(): ProfileInfo {
        return ProfileInfo(
            email = email,
            login = login,
            jokesAdded = jokesAdded
        )
    }

}