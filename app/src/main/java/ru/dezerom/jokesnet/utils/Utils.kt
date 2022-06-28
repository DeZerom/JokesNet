package ru.dezerom.jokesnet.utils

import java.security.MessageDigest
import kotlin.experimental.and


fun String.sha256(): String {
    val digest = MessageDigest.getInstance("SHA-256")
    digest.reset()
    val bytes = digest.digest(this.encodeToByteArray())
    val hex: StringBuilder = StringBuilder(bytes.size * 2)
    for (b in bytes) hex.append(String.format("%02x", b and 0xFF.toByte()))
    return hex.toString()
}