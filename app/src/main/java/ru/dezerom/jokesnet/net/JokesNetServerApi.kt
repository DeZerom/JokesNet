package ru.dezerom.jokesnet.net

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import ru.dezerom.jokesnet.net.auth.Credentials
import ru.dezerom.jokesnet.net.auth.LoginResponse

interface JokesNetServerApi {

    @POST("login")
    fun login(@Body credentials: Credentials): Call<LoginResponse>

}