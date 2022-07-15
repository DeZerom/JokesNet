package ru.dezerom.jokesnet.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import ru.dezerom.jokesnet.db.joke.DbJoke
import ru.dezerom.jokesnet.db.joke.JokeDao
import ru.dezerom.jokesnet.di.ServiceWithInterceptor
import ru.dezerom.jokesnet.net.JokesNetServerApi
import ru.dezerom.jokesnet.net.joke.NetJokeAdd
import javax.inject.Inject

class JokesRepository @Inject constructor(
    @ServiceWithInterceptor private val apiService: JokesNetServerApi,
    private val jokeDao: JokeDao
) {

    /**
     * Adds a new joke with provided [text]
     * @return true if joke was added successfully, false otherwise
     */
    suspend fun addJoke(text: String): Boolean {
        if (text.isBlank()) return false
        return withContext(Dispatchers.IO) {
            val call = apiService.addJoke(NetJokeAdd(text))
            val response = call.awaitResponse()

            val netJoke = if (response.isSuccessful) response.body() else null
            netJoke ?: return@withContext false

            val dbJoke = DbJoke(
                id = netJoke.id,
                text = netJoke.text,
                creator = netJoke.creator
            )

            jokeDao.insertJoke(dbJoke)

            true
        }
    }

}