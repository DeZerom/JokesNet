package ru.dezerom.jokesnet.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import ru.dezerom.jokesnet.di.ServiceWithInterceptor
import ru.dezerom.jokesnet.net.JokesNetServerApi
import ru.dezerom.jokesnet.screens.profile.ProfileInfo
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    @ServiceWithInterceptor private val serverApi: JokesNetServerApi
) {

    /**
     * Gets the profile info.
     * @return null if some error occurred
     */
    suspend fun getProfileInfo(): ProfileInfo? {
        return withContext(Dispatchers.IO) {
            val call = serverApi.getProfileInfo()
            val response = call.awaitResponse()

            return@withContext if (response.isSuccessful) {
                val netProfile = response.body() ?: return@withContext null
                ProfileInfo(
                    login = netProfile.login,
                    jokesAdded = netProfile.jokesAdded
                )
            } else {
                null
            }
        }
    }

}
