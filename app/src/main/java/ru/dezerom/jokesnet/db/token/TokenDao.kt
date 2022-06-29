package ru.dezerom.jokesnet.db.token

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TokenDao {

    @Query("SELECT * FROM token")
    fun selectToken(): Token?

    @Insert
    fun insertToken(token: Token)

}