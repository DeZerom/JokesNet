package ru.dezerom.jokesnet.repositories

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import ru.dezerom.jokesnet.net.FORBIDDEN
import ru.dezerom.jokesnet.net.JokesNetServerApi
import ru.dezerom.jokesnet.net.auth.Credentials
import ru.dezerom.jokesnet.net.auth.NetToken
import ru.dezerom.jokesnet.utils.sha256
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
    private val apiService: JokesNetServerApi,
    private val sharedPreferences: SharedPreferences,
    private val authService: FirebaseAuth
) {

    /**
     * Checks users credentials and saves the token in the shared preferences.
     * @return true if login and password are correct, otherwise false
     */
    suspend fun performLogin(login: String, password: String): Boolean {
        if (login == "admin" && password == "admin") {
            writeToken("1234")
            return true
        } //TODO placeholder. Firebase auth will be here later
        return withContext(Dispatchers.IO) {
            val call = apiService.login(Credentials(login, password.sha256()))
            val response = call.awaitResponse()

            if (response.isSuccessful) {
                response.body()?.token?.let {
                    sharedPreferences.edit {
                        putString(SHARED_PREFS_TOKEN_KEY, it)
                        commit()
                    }
                } ?: return@withContext false
            } else if (response.code() == FORBIDDEN) return@withContext false

            return@withContext true
        }
    }


    /**
     * Tries to get the auth token
     * @return token string if token exists, null otherwise
     */
    suspend fun getToken(): String? {
        return withContext(Dispatchers.IO) {
            val token = sharedPreferences.getString(SHARED_PREFS_TOKEN_KEY, null)
            token
        }
    }

    /**
     * Sends the token to the server to check if it valid
     * @return true if token is valid, false otherwise
     */
    suspend fun checkToken(token: String): Boolean {
        if (token == "1234") return true //TODO placeholder
        return withContext(Dispatchers.IO) {
            val netToken = NetToken(token)
            val call = apiService.checkToken(netToken)

            call.awaitResponse().isSuccessful
        }
    }

    /**
     * Tries to signInNewUser
     * @return true if new user is signed in, otherwise false
     */
    @Suppress("ControlFlowWithEmptyBody")
    suspend fun createNewUser(email: String, password: String): RegistrationStatus {
        var result: RegistrationStatus? = null
        authService.createUserWithEmailAndPassword(email, password.sha256())
            .addOnSuccessListener {
                result = RegistrationStatus(true, null)
            }
            .addOnFailureListener {
                val reason = when(it) {
                    is FirebaseAuthWeakPasswordException -> RegistrationStatus.WEAK_PASSWORD
                    is FirebaseAuthInvalidCredentialsException -> RegistrationStatus.MALFORMED_EMAIL
                    is FirebaseAuthUserCollisionException -> RegistrationStatus.ACCOUNT_EXISTS
                    else -> RegistrationStatus.UNKNOWN_ERROR
                }
                result = RegistrationStatus(false, reason)
            }

        return withContext(Dispatchers.IO) {
            while (result == null) {}
            result ?: RegistrationStatus(false, RegistrationStatus.UNKNOWN_ERROR)
        }
    }

    private fun writeToken(token: String) {
        sharedPreferences.edit {
            putString(SHARED_PREFS_TOKEN_KEY, token)
            commit()
        }
    }

    class RegistrationStatus(val isSuccessful: Boolean, val reason: Int?) {
        companion object {
            const val ACCOUNT_EXISTS = -1
            const val MALFORMED_EMAIL = -2
            const val WEAK_PASSWORD = -3
            const val UNKNOWN_ERROR = -4
        }
    }

    companion object {
        const val SHARED_PREFS_TOKEN_KEY = "token"
    }

}
