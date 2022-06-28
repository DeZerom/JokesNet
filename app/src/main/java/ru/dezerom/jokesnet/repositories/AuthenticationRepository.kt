package ru.dezerom.jokesnet.repositories

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthenticationRepository @Inject constructor() {

    /**
     * !!MOCK!!
     * Checks users credentials
     * @return true if login and password are correct, otherwise false
     */
    suspend fun checkCredentials(login: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            delay(1500)
            login == "admin" && password == "admin"
        }
    }

    /**
     * !!MOCK!!
     * Tries to signInNewUser
     * @return true if new user is signed in, otherwise false
     */
    suspend fun signInNewUser(login: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            delay(1500)
            login == "admin" && password == "admin"
        }
    }

}