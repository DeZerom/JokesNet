package ru.dezerom.jokesnet.repositories

import com.google.firebase.auth.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.dezerom.jokesnet.utils.sha256
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
    private val authService: FirebaseAuth
) {

    /**
     * `true` if user is currently logged in, `false` otherwise
     */
    val isLoggedIn: Boolean
        get() = authService.currentUser != null

    /**
     * Tries to sign in with the given credentials
     * @return [SignInStatus] instance with [SignInStatus.isSuccessful] = `true`
     * and [SignInStatus.reason] = null if credentials are valid or
     * [SignInStatus.isSuccessful] = `false` and non-null value of [SignInStatus.reason] if some
     * error occurs
     */
    @Suppress("ControlFlowWithEmptyBody")
    suspend fun performLogin(email: String, password: String): SignInStatus {
        var status: SignInStatus? = null
        authService.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                status = SignInStatus(true, null)
            }
            .addOnFailureListener {
                val reason = when(it) {
                    is FirebaseAuthInvalidCredentialsException -> SignInStatus.WRONG_PASSWORD
                    is FirebaseAuthInvalidUserException -> SignInStatus.ACCOUNT_DOES_NOT_EXISTS
                    else -> SignInStatus.UNKNOWN_ERROR
                }
                status = SignInStatus(false, reason)
            }

        return withContext(Dispatchers.IO) {
            while (status == null) {}
            status ?: SignInStatus(false, SignInStatus.UNKNOWN_ERROR)
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

    /**
     * Represents the status of creating user task
     */
    class RegistrationStatus(val isSuccessful: Boolean, val reason: Int?) {
        companion object {
            const val ACCOUNT_EXISTS = -1
            const val MALFORMED_EMAIL = -2
            const val WEAK_PASSWORD = -3
            const val UNKNOWN_ERROR = -4
        }
    }

    /**
     * Represents the status of signing in task
     */
    class SignInStatus(val isSuccessful: Boolean, val reason: Int?) {
        companion object {
            const val ACCOUNT_DOES_NOT_EXISTS = -12
            const val WRONG_PASSWORD = -13
            const val UNKNOWN_ERROR = -14
        }
    }

    companion object {
        const val SHARED_PREFS_TOKEN_KEY = "token"
    }

}
