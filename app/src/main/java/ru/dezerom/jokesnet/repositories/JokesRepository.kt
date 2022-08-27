package ru.dezerom.jokesnet.repositories

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
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
import ru.dezerom.jokesnet.utils.addResultListeners
import ru.dezerom.jokesnet.utils.addSuccessListeners
import java.util.HashMap
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
        if (text.isBlank()) {
            return false
        }
        val creator = authService.currentUser?.email ?: return false

        val docRef = jokesRef.add(NetJoke(text = text, creator = creator))
            .addResultListeners() ?: return false
        if (!createViewsDocument(docRef, creator)) {
            docRef.delete()
            return false
        }
        incrementJokesAddedAmount()

        return true
    }

    private suspend fun createViewsDocument(
        forWhat: DocumentReference,
        creatorEmail: String
    ): Boolean {
        return forWhat.collection(FirestoreNames.JOKE_VIEWS_COLLECTION)
            .document(FirestoreNames.JOKE_VIEWS_DOCUMENT)
            .set(hashMapOf(FirestoreNames.JOKE_VIEWERS_FIELD to listOf(creatorEmail)))
            .addSuccessListeners()
    }

    private suspend fun incrementJokesAddedAmount() {
        val docId = sharedPrefs.getString(SharedPrefsNames.PROFILE_ID, null)
            ?: getUserProfileId() ?: return

        profileRef.document(docId)
            .update(FirestoreNames.USER_JOKES_ADDED_FIELD, FieldValue.increment(1))
    }

    private suspend fun getUserProfileId(): String? {
        val email = authService.currentUser?.email ?: return null

        val snapshot = profileRef.whereEqualTo(FirestoreNames.USER_EMAIL_FIELD, email).get()
            .addResultListeners()

        val id = snapshot?.documents?.firstOrNull()?.id ?: return null
        sharedPrefs.edit {
            putString(SharedPrefsNames.PROFILE_ID, id)
            apply()
        }

        return id
    }

    /**
     * @return A [Joke] that user hasn't seen yet
     */
    suspend fun getNewJoke(): Joke? {

    }

    private suspend fun get

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
