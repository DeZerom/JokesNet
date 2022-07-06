package ru.dezerom.jokesnet.net

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import ru.dezerom.jokesnet.repositories.AuthenticationRepository

class AuthInterceptor (
    private val sharedPreferences: SharedPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sharedPreferences.getString(AuthenticationRepository.SHARED_PREFS_TOKEN_KEY,
        "")

        val request = chain.request().newBuilder()
            .addHeader(JokesNetServerApi.AUTH_HEADER, token ?: "")
            .build()

        return chain.proceed(request)
    }
}