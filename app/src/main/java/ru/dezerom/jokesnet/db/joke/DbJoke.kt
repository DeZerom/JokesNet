package ru.dezerom.jokesnet.db.joke

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jokes")
class DbJoke(
    @PrimaryKey val id: String,
    val text: String,
    val creator: String
)