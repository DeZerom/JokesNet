package ru.dezerom.jokesnet.repositories

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import ru.dezerom.jokesnet.db.joke.DbJoke
import ru.dezerom.jokesnet.db.joke.JokeDao
import ru.dezerom.jokesnet.di.JokesReference
import ru.dezerom.jokesnet.di.ServiceWithInterceptor
import ru.dezerom.jokesnet.net.JokesNetServerApi
import ru.dezerom.jokesnet.net.joke.NetJoke
import ru.dezerom.jokesnet.net.joke.NetJokeAdd
import javax.inject.Inject

class JokesRepository @Inject constructor(
    @JokesReference private val jokesRef: CollectionReference,
) {

    /**
     * Adds a new joke with provided [text]
     * @return true if joke was added successfully, false otherwise
     */
    @Suppress("ControlFlowWithEmptyBody")
    suspend fun addJoke(text: String): Boolean {
        if (text.isBlank()) return false

        var res: Boolean? = null
        jokesRef.add(NetJoke(text, "placeholder"))
            .addOnSuccessListener {
                res = true
            }.addOnFailureListener {
                res = false
            }
        return withContext(Dispatchers.IO) {
            while (res == null) {}
            res ?: false
        }
    }

}