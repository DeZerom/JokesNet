package ru.dezerom.jokesnet.repositories

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.dezerom.jokesnet.di.JokesReference
import ru.dezerom.jokesnet.di.UsersReference
import ru.dezerom.jokesnet.net.FirestoreNames
import ru.dezerom.jokesnet.net.joke.NetJoke
import ru.dezerom.jokesnet.screens.joke_view.Joke
import ru.dezerom.jokesnet.utils.SharedPrefsNames
import javax.inject.Inject

class JokesRepository @Inject constructor(
    @JokesReference private val jokesRef: CollectionReference,
    private val authService: FirebaseAuth,
    private val sharedPrefs: SharedPreferences,
    @UsersReference private val profileRef: CollectionReference
) {

    /**
     * Adds a new joke with provided [text]. Increments the amount of the jokes that user has added
     * @return true if joke was added successfully, false otherwise
     */
    suspend fun addJoke(text: String): Boolean {
        if (text.isBlank()) return false
        val creator = authService.currentUser?.email ?: return false

        var resAdding: Boolean? = null
        jokesRef.add(NetJoke(text, creator))
            .addOnSuccessListener {
                resAdding = true
            }.addOnFailureListener {
                resAdding = false
            }
        incrementJokesAddedAmount()

        @Suppress("ControlFlowWithEmptyBody")
        return withContext(Dispatchers.IO) {
            while (resAdding == null) {}
            resAdding ?: false
        }
    }

    private suspend fun incrementJokesAddedAmount(): Boolean {
        val docId = sharedPrefs.getString(SharedPrefsNames.PROFILE_ID, null)
            ?: getUserProfileId() ?: return false

        var res: Boolean? = null
        profileRef.document(docId)
            .update(FirestoreNames.USER_JOKES_ADDED_FIELD, FieldValue.increment(1))
            .addOnSuccessListener { res = true }
            .addOnFailureListener { res = false }

        return withContext(Dispatchers.IO) {
            @Suppress("ControlFlowWithEmptyBody")
            while (res == null) {}
            res ?: false
        }
    }

    private suspend fun getUserProfileId(): String? {
        val email = authService.currentUser?.email ?: return null

        var res: String? = null
        var successful = true
        profileRef.whereEqualTo(FirestoreNames.USER_EMAIL_FIELD, email).get()
            .addOnSuccessListener {
                if (it.documents.isEmpty()) {
                    successful = false
                    return@addOnSuccessListener
                }
                res = it.documents.first().id
                sharedPrefs.edit {
                    putString(SharedPrefsNames.PROFILE_ID, res)
                    apply()
                }
            }
            .addOnFailureListener {
                successful = false
            }

        return withContext(Dispatchers.IO) {
            @Suppress("ControlFlowWithEmptyBody")
            while (res == null && successful) {}
            res
        }
    }

    /**
     * @return A [Joke] that user hasn't seen yet
     */
    suspend fun getNewJoke(): Joke? { //TODO placeholder
        return withContext(Dispatchers.IO) {
            delay(1500)
            Joke(
                text = "хи хи хаха",
                creator = "System",
                views = 666
            )
        }
    }

    /**
     * @return a random [Joke]
     */
    suspend fun getRandomJoke(): Joke? { //TODO placeholder
        return withContext(Dispatchers.IO) {
            delay(1500)
            null
        }
    }

}