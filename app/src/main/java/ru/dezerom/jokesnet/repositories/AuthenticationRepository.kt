package ru.dezerom.jokesnet.repositories

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.google.firebase.auth.*
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
     * Tries to create a new user with given [email], [login] and [password]
     * @return [RegistrationStatus] instance with value of [RegistrationStatus.isSuccessful] equals
     * to true if new user is signed in, otherwise false. If [RegistrationStatus.isSuccessful] is
     * true, then [RegistrationStatus.reason] is null. Otherwise it is non nul and equals one of the
     * following. [RegistrationStatus.UNKNOWN_ERROR], [RegistrationStatus.WEAK_PASSWORD],
     * [RegistrationStatus.ACCOUNT_EXISTS], [RegistrationStatus.MALFORMED_EMAIL]
     */
    @Suppress("ControlFlowWithEmptyBody")
    suspend fun createNewUser(email: String, login: String, password: String): RegistrationStatus {
        var regStatus: RegistrationStatus? = null
        authService.createUserWithEmailAndPassword(email, password.sha256())
            .addOnSuccessListener {
                regStatus = RegistrationStatus(true, null)
            }
            .addOnFailureListener {
                val reason = when (it) {
                    is FirebaseAuthWeakPasswordException -> RegistrationStatus.WEAK_PASSWORD
                    is FirebaseAuthInvalidCredentialsException -> RegistrationStatus.MALFORMED_EMAIL
                    is FirebaseAuthUserCollisionException -> RegistrationStatus.ACCOUNT_EXISTS
                    else -> RegistrationStatus.UNKNOWN_ERROR
                }
                regStatus = RegistrationStatus(false, reason)
            }

        var res = withContext(Dispatchers.IO) {
            while (regStatus == null) {}
            regStatus ?: RegistrationStatus(false, RegistrationStatus.UNKNOWN_ERROR)
        }

        res = if (res.isSuccessful) {
            val isLoginChanged = changeLogin(login)
            RegistrationStatus(
                isSuccessful = isLoginChanged,
                reason = if (isLoginChanged) null else RegistrationStatus.UNKNOWN_ERROR
            )
        } else {
            res
        }

        return res
    }

    /**
     * Changes user's login
     * @return true if successful, false otherwise
     */
    @Suppress("ControlFlowWithEmptyBody")
    suspend fun changeLogin(newLogin: String): Boolean {
        var result: Boolean? = null
        authService.currentUser?.updateProfile(
            UserProfileChangeRequest.Builder().setDisplayName(newLogin).build())
            ?.addOnSuccessListener {
                result = true
            }
            ?.addOnFailureListener {
                result = false
            }
        return withContext(Dispatchers.IO) {
            while (result == null) {}
            result ?: false
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
