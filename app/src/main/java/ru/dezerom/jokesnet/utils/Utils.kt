package ru.dezerom.jokesnet.utils

import android.util.Log
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import kotlin.experimental.and

/**
 * Encodes string using sha256 algorithm
 * @return encoded string
 */
fun String.sha256(): String {
    val digest = MessageDigest.getInstance("SHA-256")
    digest.reset()
    val bytes = digest.digest(this.encodeToByteArray())
    val hex: StringBuilder = StringBuilder(bytes.size * 2)
    for (b in bytes) hex.append(String.format("%02x", b and 0xFF.toByte()))
    return hex.toString()
}

/**
 * Adds listeners for the task
 * @return `true` if task was successfully done, `false` otherwise
 */
suspend fun Task<*>.addSuccessListeners(): Boolean {
    var res: Boolean? = null
    this
        .addOnSuccessListener { res = true }
        .addOnFailureListener { res = false }

    return withContext(Dispatchers.IO) {
        @Suppress("ControlFlowWithEmptyBody")
        while (res == null) {}
        res ?: false
    }
}

/**
 * Adds listeners for the task
 * @return task result or null, if onFailureListener was called
 */
suspend fun <E> Task<E>.addResultListeners(): E? {
    var successful: Boolean? = null
    var res: E? = null
    this
        .addOnSuccessListener {
            res = it
            successful = true
        }
        .addOnFailureListener {
            successful = false
        }

    return withContext(Dispatchers.IO) {
        @Suppress("ControlFlowWithEmptyBody")
        while (successful == null) {}
        res
    }
}