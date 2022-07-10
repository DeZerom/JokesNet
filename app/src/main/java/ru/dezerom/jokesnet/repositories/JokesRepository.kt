package ru.dezerom.jokesnet.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.dezerom.jokesnet.di.ServiceWithInterceptor
import ru.dezerom.jokesnet.net.JokesNetServerApi
import javax.inject.Inject

class JokesRepository @Inject constructor(
    @ServiceWithInterceptor private val apiService: JokesNetServerApi
) {

    /**
     * Adds a new joke with provided [text]
     * @return true if joke was added successfully, false otherwise
     */
    suspend fun addJoke(text: String): Boolean {
        if (text.isBlank()) return false
        return withContext(Dispatchers.IO) {

        }
    }

    private suspend fun getJokeFromDb() {

    }

}