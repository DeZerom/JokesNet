package ru.dezerom.jokesnet.repositories

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import retrofit2.http.HTTP
import ru.dezerom.jokesnet.db.token.Token
import ru.dezerom.jokesnet.db.token.TokenDao
import ru.dezerom.jokesnet.net.FORBIDDEN
import ru.dezerom.jokesnet.net.JokesNetServerApi
import ru.dezerom.jokesnet.net.auth.Credentials
import ru.dezerom.jokesnet.utils.sha256
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
    private val apiService: JokesNetServerApi,
    private val tokenDao: TokenDao
) {

    /**
     * Checks users credentials and saves the token in the database.
     * @return true if login and password are correct, otherwise false
     */
    suspend fun performLogin(login: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            val call = apiService.login(Credentials(login, password.sha256()))
            val response = call.awaitResponse()

            if (response.isSuccessful) {
                response.body()?.token?.let {
                    tokenDao.insertToken(Token(it))
                } ?: return@withContext false
            } else if (response.code() == FORBIDDEN) return@withContext false

            return@withContext true
        }
    }

    suspend fun getToken(): Token? {
        return withContext(Dispatchers.IO) {
            tokenDao.selectToken()
        }
    }

    /**
     * !!MOCK!!
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

}
