package ru.dezerom.jokesnet.repositories

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.dezerom.jokesnet.di.SharedPrefsProvider
import ru.dezerom.jokesnet.di.UsersReference
import ru.dezerom.jokesnet.net.FirestoreNames
import ru.dezerom.jokesnet.net.profile.NetProfileInfo
import ru.dezerom.jokesnet.screens.profile.ProfileInfo
import ru.dezerom.jokesnet.utils.SharedPrefsNames
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    @UsersReference private val profileRef: CollectionReference,
    private val authService: FirebaseAuth,
    private val sharedPrefs: SharedPreferences
) {

    /**
     * Tries to get the [ProfileInfo] from the server.
     * @return [ProfileInfo] instance if successful, null otherwise
     */
    suspend fun getProfileInfo(): ProfileInfo? {
        val email = authService.currentUser?.email ?: return null
        var profileInfo = getProfileInfoFromApi(email)

        if (profileInfo == null) {
            val successful = insertProfileInfo(
                ProfileInfo(
                    email = email,
                    login = authService.currentUser?.displayName ?: return null,
                    jokesAdded = 0
                )
            )
            if (successful) profileInfo = getProfileInfoFromApi(email)
            else return null
        }

        return profileInfo
    }

    private suspend fun getProfileInfoFromApi(email: String): ProfileInfo? {
        var profileInfo: NetProfileInfo? = null
        var successful = true
        profileRef.whereEqualTo(FirestoreNames.USER_EMAIL_FIELD, email).get()
            .addOnSuccessListener {
                if (it.isEmpty) successful = false
                else {
                    profileInfo = it.documents.first().toObject()
                    if (!sharedPrefs.contains(SharedPrefsNames.PROFILE_ID)) {
                        sharedPrefs.edit {
                            putString(SharedPrefsNames.PROFILE_ID, it.documents.first().id)
                            apply()
                        }
                    }
                }
            }
            .addOnFailureListener {
                successful = false
            }

        return withContext(Dispatchers.IO) {
            @Suppress("ControlFlowWithEmptyBody")
            while (profileInfo == null && successful) {}
            profileInfo?.toProfileInfo()
        }
    }

    private suspend fun insertProfileInfo(profileInfo: ProfileInfo): Boolean {
        var result: Boolean? = null
        profileRef.add(profileInfo)
            .addOnSuccessListener {
                result = true
            }
            .addOnFailureListener {
                result = false
            }

        return withContext(Dispatchers.IO) {
            @Suppress("ControlFlowWithEmptyBody")
            while (result == null) {}
            result ?: false
        }
    }

    /**
     * Tries to update user's login.
     * @return `true` if successful, `false` otherwise
     */
    suspend fun updateUserLogin(newLogin: String): Boolean {
        val docId = sharedPrefs.getString(SharedPrefsNames.PROFILE_ID, "") ?: return false

        var res: Boolean? = null
        profileRef.document(docId).update(FirestoreNames.USER_LOGIN_FIELD, newLogin)
            .addOnSuccessListener {
                res = true
            }
            .addOnFailureListener {
                res = false
            }

        return withContext(Dispatchers.IO) {
            @Suppress("ControlFlowWithEmptyBody")
            while (res == null) {}
            res ?: false
        }
    }

}
