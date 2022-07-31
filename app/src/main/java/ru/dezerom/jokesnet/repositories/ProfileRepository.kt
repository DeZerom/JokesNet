package ru.dezerom.jokesnet.repositories

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import ru.dezerom.jokesnet.db.profile.DbProfileInfo
import ru.dezerom.jokesnet.db.profile.ProfileInfoDao
import ru.dezerom.jokesnet.di.ServiceWithInterceptor
import ru.dezerom.jokesnet.net.JokesNetServerApi
import ru.dezerom.jokesnet.net.profile.NetProfileInfo
import ru.dezerom.jokesnet.screens.profile.ProfileInfo
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    @ServiceWithInterceptor private val serverApi: JokesNetServerApi,
    private val profileDao: ProfileInfoDao
) {

    /**
     * Gets profile info. If profile info saved in the database exists and is valid returns it.
     * Otherwise tries to get it from the server
     * @return [ProfileInfo] or null if some error occurred
     */
    suspend fun getProfileInfo(): ProfileInfo? {
        return ProfileInfo("admin", 666) //TODO placeholder
        return withContext(Dispatchers.IO) {
            val dbProfile = profileDao.selectProfileInfo()
            val id = dbProfile?.id ?: 0
            dbProfile?.let {
                if (it.isValid) {
                    return@withContext ProfileInfo(
                        login = it.login,
                        jokesAdded = it.jokesAdded
                    )
                }
            }

            val profile = getProfileInfoFromApi() ?: return@withContext null
            Log.i("ProfileRepository", "dbProfile = ${dbProfile ?: "null dbProfile"}, id = $id, profile = $profile")
            if (id != 0) updateProfileInfo(profileInfo = profile, id)
            else insertProfileInfo(profileInfo = profile)

            profile
        }
    }

    private suspend fun insertProfileInfo(profileInfo: ProfileInfo) {
        withContext(Dispatchers.IO) {
            profileDao.insertProfileInfo(DbProfileInfo(
                id = 0,
                login = profileInfo.login,
                jokesAdded = profileInfo.jokesAdded,
                isValid = true
            ))
        }
    }

    private suspend fun updateProfileInfo(profileInfo: ProfileInfo, id: Int) {
        withContext(Dispatchers.IO) {
            profileDao.updateProfileInfo(
                DbProfileInfo(
                id = id,
                login = profileInfo.login,
                jokesAdded = profileInfo.jokesAdded,
                isValid = true
            )
            )
        }
    }

    private suspend fun getProfileInfoFromApi(): ProfileInfo? {
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
