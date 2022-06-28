package ru.dezerom.jokesnet.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.dezerom.jokesnet.db.token.Token
import ru.dezerom.jokesnet.db.token.TokenDao

@Database(entities = [Token::class], version = 1, exportSchema = true)
abstract class JokesNetDatabase: RoomDatabase() {
    abstract fun getTokenDao(): TokenDao
}