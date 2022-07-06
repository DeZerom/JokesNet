package ru.dezerom.jokesnet.di

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.dezerom.jokesnet.net.AuthInterceptor
import ru.dezerom.jokesnet.net.JokesNetServerApi
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    fun provideHttpClient(sharedPreferences: SharedPreferences): OkHttpClient {
        val interceptor = AuthInterceptor(sharedPreferences)
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @RetrofitWithInterceptor
    fun provideRetrofitWithInterceptor(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    fun provideService(retrofit: Retrofit): JokesNetServerApi {
        return retrofit.create(JokesNetServerApi::class.java)
    }

    @Provides
    @ServiceWithInterceptor
    fun provideServiceWithInterceptor(@RetrofitWithInterceptor retrofit: Retrofit): JokesNetServerApi {
        return retrofit.create(JokesNetServerApi::class.java)
    }

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitWithInterceptor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ServiceWithInterceptor
