package ru.dezerom.jokesnet.net

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import ru.dezerom.jokesnet.net.auth.Credentials
import ru.dezerom.jokesnet.net.auth.LoginResponse
import ru.dezerom.jokesnet.net.auth.NetToken

interface JokesNetServerApi {

    @POST("login")
    fun login(@Body credentials: Credentials): Call<LoginResponse>

    @POST("/registration")
    fun signIn(@Body credentials: Credentials): Call<Unit>

    @POST("/check-token")
    fun checkToken(@Body token: NetToken): Call<Unit>

}