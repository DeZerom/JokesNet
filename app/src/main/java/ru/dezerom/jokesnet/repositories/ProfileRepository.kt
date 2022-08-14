package ru.dezerom.jokesnet.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.dezerom.jokesnet.di.UsersReference
import ru.dezerom.jokesnet.net.FirestoreNames
import ru.dezerom.jokesnet.net.profile.NetProfileInfo
import ru.dezerom.jokesnet.screens.profile.ProfileInfo
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    @UsersReference private val profileRef: CollectionReference,
    private val authService: FirebaseAuth
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
                else profileInfo = it.documents.first().toObject()
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

}
