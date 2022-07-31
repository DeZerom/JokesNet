package ru.dezerom.jokesnet.db.joke

import androidx.room.*

@Dao
interface JokeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJoke(joke: DbJoke)

    @Update
    suspend fun updateJoke(joke: DbJoke)

}
