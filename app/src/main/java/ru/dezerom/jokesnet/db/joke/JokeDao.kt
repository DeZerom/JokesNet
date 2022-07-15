package ru.dezerom.jokesnet.db.joke

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface JokeDao {

    @Insert
    fun insertJoke(joke: DbJoke)

    @Update
    fun updateJoke(joke: DbJoke)

}
