package ru.dezerom.jokesnet.repositories

import android.content.SharedPreferences
import androidx.core.content.edit
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
    private val sharedPreferences: SharedPreferences
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
    suspend fun signInNewUser(login: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            val call = apiService.signIn(Credentials(login, password.sha256()))
            val response = call.awaitResponse()

            response.isSuccessful
        }
    }

    private fun writeToken(token: String) {
        sharedPreferences.edit {
            putString(SHARED_PREFS_TOKEN_KEY, token)
            commit()
        }
    }

    companion object {
        const val SHARED_PREFS_TOKEN_KEY = "token"
    }

}
