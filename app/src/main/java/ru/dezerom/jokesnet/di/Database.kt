package ru.dezerom.jokesnet.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.dezerom.jokesnet.db.JokesNetDatabase
import ru.dezerom.jokesnet.db.joke.JokeDao
import ru.dezerom.jokesnet.db.profile.ProfileInfoDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): JokesNetDatabase {
        return Room.databaseBuilder(context, JokesNetDatabase::class.java, "JokesNetDatabase")
            .addMigrations(JokesNetDatabase.MIGRATION_1_2)
            .build()
    }

    @Provides
    fun provideProfileInfoDao(db: JokesNetDatabase): ProfileInfoDao {
        return db.getProfileInfo()
    }

    @Provides
    fun provideJokeDao(db: JokesNetDatabase): JokeDao {
        return db.getJokeDao()
    }

}