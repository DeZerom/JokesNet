package ru.dezerom.jokesnet.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepository @Inject constructor() {

    /**
     * Gets the profile info.
     * @return null if some error occurred
     */
    suspend fun getProfileInfo(): String? {
        return withContext(Dispatchers.IO) {
            delay(1500)
            null
        }
    }

}