package ru.dezerom.jokesnet.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class AuthenticationRepository {

    /**
     * Check users credentials
     */
    suspend fun checkCredentials(login: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            delay(1500)
            login == "admin" && password == "admin"
        }
    }

}